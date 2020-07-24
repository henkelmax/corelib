package de.maxhenkel.corelib.config;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.electronwill.nightconfig.core.conversion.ObjectConverter;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.file.FileNotFoundAction;
import com.electronwill.nightconfig.core.file.GenericBuilder;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class DynamicConfig {

    protected CommentedFileConfig config;
    private ObjectConverter converter;

    public DynamicConfig() {
        converter = new ObjectConverter();
    }

    public void init(Path configFile, Path defaultConfigFile) {
        boolean createDefaults = !configFile.toFile().exists() && !defaultConfigFile.toFile().exists();
        GenericBuilder<CommentedConfig, CommentedFileConfig> builder = CommentedFileConfig.builder(configFile).onFileNotFound(FileNotFoundAction.CREATE_EMPTY).autosave();
        if (defaultConfigFile != null && defaultConfigFile.toFile().exists()) {
            builder.defaultData(defaultConfigFile);
        }
        config = builder.build();

        config.load();

        if (createDefaults) {
            setDefaults();
        }
        onLoad();
    }

    public void init(Path configFile) {
        init(configFile, null);
    }

    protected void setDefaults() {

    }

    protected void onLoad() {

    }

    public <T> T get(String path, T defaultValue) {
        checkLoaded();
        return config.getOrElse(path, defaultValue);
    }

    public List<String> getSubValues(String path) {
        checkLoaded();
        Config cfg;
        if (path == null || path.isEmpty()) {
            cfg = config;
        } else {
            cfg = config.<Config>getOrElse(path, null);
        }
        if (cfg != null) {
            return cfg.entrySet().stream().map(UnmodifiableConfig.Entry::getKey).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public List<String> getSubValues() {
        checkLoaded();
        return getSubValues(null);
    }

    @Nullable
    private Config getSubConfig(String path) {
        checkLoaded();
        return config.<Config>getOrElse(path, null);
    }

    public <T> T getObject(String path, Supplier<T> object, Supplier<T> defaultValue) {
        checkLoaded();
        Config subConfig = getSubConfig(path);
        if (subConfig == null) {
            return defaultValue.get();
        }
        return converter.toObject(subConfig, object);
    }

    @Nullable
    public <T> T getObject(String path, Supplier<T> object) {
        return getObject(path, object, () -> null);
    }

    public <T> T setObject(String path, T object) {
        checkLoaded();
        Config config = converter.toConfig(object, Config::inMemoryUniversal);
        set(path, config);
        return object;
    }

    public <T> T set(String path, T value) {
        checkLoaded();
        return config.set(path, value);
    }

    private void checkLoaded() {
        if (!isLoaded()) {
            throw new IllegalStateException("Config not loaded");
        }
    }

    public boolean isLoaded() {
        return config != null;
    }

    public static enum DynamicConfigType {
        SERVER, COMMON
    }

}

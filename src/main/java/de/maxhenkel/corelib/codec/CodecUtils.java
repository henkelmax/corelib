package de.maxhenkel.corelib.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CodecUtils {

    public static <T> Optional<Tag> toNBT(Codec<T> codec, T object) {
        return codec.encodeStart(NbtOps.INSTANCE, object).result();
    }

    public static <T> Optional<T> fromNBT(Codec<T> codec, Tag nbt) {
        return codec.decode(NbtOps.INSTANCE, nbt).result().map(Pair::getFirst);
    }

    public static <T> Optional<T> fromNBT(Codec<T> codec, String nbtString) {
        return codec.decode(NbtOps.INSTANCE, StringTag.valueOf(nbtString)).result().map(Pair::getFirst);
    }

    public static <U> StreamCodec<RegistryFriendlyByteBuf, Optional<U>> optionalStreamCodec(StreamCodec<RegistryFriendlyByteBuf, U> streamCodec) {
        return new StreamCodec<>() {
            @Override
            public Optional<U> decode(RegistryFriendlyByteBuf buf) {
                if (buf.readBoolean()) {
                    return Optional.of(streamCodec.decode(buf));
                }
                return Optional.empty();
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buf, Optional<U> t) {
                if (t.isPresent()) {
                    buf.writeBoolean(true);
                    streamCodec.encode(buf, t.get());
                } else {
                    buf.writeBoolean(false);
                }
            }
        };
    }

    public static <U> StreamCodec<ByteBuf, Optional<U>> optionalStreamCodecByteBuf(StreamCodec<ByteBuf, U> streamCodec) {
        return new StreamCodec<>() {
            @Override
            public Optional<U> decode(ByteBuf buf) {
                if (buf.readBoolean()) {
                    return Optional.of(streamCodec.decode(buf));
                }
                return Optional.empty();
            }

            @Override
            public void encode(ByteBuf buf, Optional<U> t) {
                if (t.isPresent()) {
                    buf.writeBoolean(true);
                    streamCodec.encode(buf, t.get());
                } else {
                    buf.writeBoolean(false);
                }
            }
        };
    }

    public static <T> StreamCodec<RegistryFriendlyByteBuf, List<T>> listStreamCodec(StreamCodec<RegistryFriendlyByteBuf, T> codec) {
        return new StreamCodec<>() {
            @Override
            public void encode(RegistryFriendlyByteBuf buf, List<T> list) {
                buf.writeInt(list.size());
                for (T t : list) {
                    codec.encode(buf, t);
                }
            }

            @Override
            public List<T> decode(RegistryFriendlyByteBuf buf) {
                int length = buf.readInt();
                List<T> list = new ArrayList<>(length);
                for (int i = 0; i < length; i++) {
                    T t = codec.decode(buf);
                    list.add(t);
                }
                return list;
            }
        };
    }

    public static <T> StreamCodec<ByteBuf, List<T>> listStreamCodecByteBuf(StreamCodec<ByteBuf, T> codec) {
        return new StreamCodec<>() {
            @Override
            public void encode(ByteBuf buf, List<T> list) {
                buf.writeInt(list.size());
                for (T t : list) {
                    codec.encode(buf, t);
                }
            }

            @Override
            public List<T> decode(ByteBuf buf) {
                int length = buf.readInt();
                List<T> list = new ArrayList<>(length);
                for (int i = 0; i < length; i++) {
                    T t = codec.decode(buf);
                    list.add(t);
                }
                return list;
            }
        };
    }

}

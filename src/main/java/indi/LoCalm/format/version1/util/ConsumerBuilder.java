//package indi.LoCalm.format.version1.util;
//
//
//import cn.hutool.core.collection.CollUtil;
//import org.jetbrains.annotations.Contract;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//
//
///**
// * CollUtil.ConsumerBuilder
// *
// * @author LoCalm
// */
//public class ConsumerBuilder<T> implements Serializable {
//	private ConsumerBuilder() {
//	}
//
//	private final List<CollUtil.Consumer<T>> consumers = new ArrayList<>();
//
//	public ConsumerBuilder<T> add(CollUtil.Consumer<T> consumer) {
//		if (consumer != null) {
//			consumers.add(consumer);
//		}
//		return this;
//	}
//
//	public ConsumerBuilder<T> addAll(List<CollUtil.Consumer<T>> consumer) {
//		if (CollUtil.isNotEmpty(consumer)) {
//			consumers.addAll(consumer);
//		}
//		return this;
//	}
//
//	@Nullable
//	public CollUtil.Consumer<T> build() {
//		if (CollUtil.isEmpty(consumers)) return null;
//		return (value, index) -> consumers.forEach(consumer -> consumer.accept(value, index));
//	}
//
//	@NotNull
//	@Contract(" -> new")
//	public static <T> ConsumerBuilder<T> builder() {
//		return new ConsumerBuilder<>();
//	}
//}
//

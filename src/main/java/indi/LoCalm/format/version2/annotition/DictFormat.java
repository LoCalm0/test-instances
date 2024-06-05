//package indi.LoCalm.format.version2.annotition;
//
//import org.springframework.core.annotation.AliasFor;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//
///**
// * 字典格式化
// *
// * @author LoCalm
// */
//@Target(ElementType.FIELD)
//@Retention(RetentionPolicy.RUNTIME)
//public @interface DictFormat {
//
//    @AliasFor("fieldName")
//    String value() default "";
//
//    @AliasFor("value")
//    String fieldName() default "";
//
//    DictCode code();
//
//    enum DictCode {
//        /**
//         * CONT-变更类型
//         */
//        CONT_ALTER_TYPE,
//        /**
//         * CSR-风险类型
//         */
//        CSR_RISK_TYPE,
//        /**
//         * CSR-风险等级
//         */
//        CSR_RISK_LEVEL,
//        /**
//         * PS-包装方式
//         */
//        PS_PACKING,
//        /**
//         * PS-服务类型
//         */
//        PS_TYPE_OF_SERVICE,
//        /**
//         * PS-发货状态
//         */
//        PS_SHIPMENT_STATUS,
//        /**
//         * PS-服务优先级
//         */
//        PS_SERVICE_PRIORITY,
//        /**
//         * PS-验收状态
//         */
//        PS_ACCEPTANCE_STATUS,
//        /**
//         * PS-运输工具
//         */
//        PS_MEANS_OF_TRANSPORT,
//        /**
//         * PS-处理和服务状态
//         */
//        PS_PROCESSING_AND_SERVICE_STATUS
//
//
//    }
//
//}

/*
 *  Copyright (c) 2018-2028, sy.im-tristone.com All rights reserved.
 *  Author: im-tristone 三岩信息 (0731-85205199)
 */
package indi.LoCalm.storage.plugins.model;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.tenant.mp.TenantEntity;

/**
 * 模型表 实体类
 *
 * @author LoCalm
 */
@Data
@TableName("im_bs_model_table")
@ApiModel(value = "ModelTable对象", description = "模型表")
@EqualsAndHashCode(callSuper = true)
public class ModelTableEntity extends TenantEntity {

	@ApiModelProperty(value = "表名")
	private String tableName;
	@ApiModelProperty(value = "表备注")
	private String tableRemark;

}

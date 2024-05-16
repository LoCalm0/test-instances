/*
 *  Copyright (c) 2018-2028, sy.im-tristone.com All rights reserved.
 *  Author: im-tristone 三岩信息 (0731-85205199)
 */
package indi.LoCalm.storage.plugins.model;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.imtristone.core.boot.ctrl.BladeController;
import com.imtristone.develop.entity.ModelTableFieldsEntity;
import com.imtristone.develop.service.IModelTableFieldsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 模型表字段 控制器
 *
 * @author LoCalm
 */
@RestController
@AllArgsConstructor
@RequestMapping("/modelTableFields")
@Api(value = "拓展信息模型属性表", tags = "拓展信息模型属性表接口")
public class ModelTableFieldsController extends BladeController {

	private final IModelTableFieldsService modelTableFieldsService;

	/**
	 * 模型表字段 详情
	 */
	@ApiLog("模型表字段-详情")
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入modelTableFields")
	public R<List<ModelTableFieldsEntity>> detail(@RequestParam Long parentId) {
		return R.data(modelTableFieldsService.list(Wrappers.<ModelTableFieldsEntity>lambdaQuery().in(ModelTableFieldsEntity::getModelTableId,parentId)));
	}

	/**
	 * 模型表字段 新增或修改
	 */
	@ApiLog("模型表字段-新增或修改")
	@PostMapping("/submit")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "新增或修改", notes = "传入modelTableFields")
	public R<Boolean> submit(@Valid @RequestBody List<ModelTableFieldsEntity> modelTableFields,@RequestParam List<Long> ids) {
		boolean flag0 = false;
		if (CollUtil.isNotEmpty(ids)) {
			flag0 = modelTableFieldsService.deleteLogic(ids);
		}
		boolean flag1 = modelTableFieldsService.saveOrUpdateBatch(modelTableFields);
		return R.status(flag0 || flag1);
	}

}

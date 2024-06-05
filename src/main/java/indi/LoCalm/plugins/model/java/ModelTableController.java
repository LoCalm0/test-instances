///*
// *  Copyright (c) 2018-2028, sy.im-tristone.com All rights reserved.
// *  Author: im-tristone 三岩信息 (0731-85205199)
// */
//package indi.LoCalm.storage.plugins.model;
//
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.core.toolkit.Wrappers;
//import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
//import com.imtristone.core.boot.ctrl.BladeController;
//import com.imtristone.develop.entity.ModelTableEntity;
//import com.imtristone.develop.entity.ModelTableFieldsEntity;
//import com.imtristone.develop.service.IModelTableFieldsService;
//import com.imtristone.develop.service.IModelTableService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
//import lombok.AllArgsConstructor;
//import org.springblade.core.log.annotation.ApiLog;
//import org.springblade.core.mp.support.Condition;
//import org.springblade.core.mp.support.Query;
//import org.springblade.core.tool.api.R;
//import org.springframework.web.bind.annotation.*;
//import springfox.documentation.annotations.ApiIgnore;
//
//import javax.validation.Valid;
//import java.util.List;
//import java.util.Map;
//
///**
// * 模型表 控制器
// *
// * @author LoCalm
// */
//@RestController
//@AllArgsConstructor
//@RequestMapping("/modelTable")
//@Api(value = "模型表", tags = "模型表接口")
//public class ModelTableController extends BladeController {
//
//	private final IModelTableService modelTableService;
//	private final IModelTableFieldsService modelTableFieldsService;
//
//	/**
//	 * 模型表 详情
//	 */
//	@ApiLog("模型表-详情")
//	@GetMapping("/detail")
//	@ApiOperationSupport(order = 1)
//	@ApiOperation(value = "详情", notes = "传入modelTable")
//	public R<ModelTableEntity> detail(ModelTableEntity entity) {
//		return R.data(modelTableService.getOne(Condition.getQueryWrapper(entity)));
//	}
//
//	/**
//	 * 模型表 分页
//	 */
//	@ApiLog("模型表-分页查询")
//	@GetMapping("/list")
//	@ApiOperationSupport(order = 2)
//	@ApiOperation(value = "分页", notes = "传入modelTable")
//	public R<IPage<ModelTableEntity>> list(@ApiIgnore @RequestParam Map<String, Object> params, Query query) {
//		return R.data(modelTableService.page(Condition.getPage(query), Condition.getQueryWrapper(params, ModelTableEntity.class)));
//	}
//
//	/**
//	 * 模型表 新增或修改
//	 */
//	@ApiLog("模型表-新增或修改")
//	@PostMapping("/submit")
//	@ApiOperationSupport(order = 6)
//	@ApiOperation(value = "新增或修改", notes = "传入modelTable")
//	public R<Long> submit(@Valid @RequestBody ModelTableEntity entity) {
//		modelTableService.saveOrUpdate(entity);
//		return R.data(entity.getId());
//	}
//
//	/**
//	 * 模型表 删除
//	 */
//	@ApiLog("模型表-删除")
//	@PostMapping("/remove")
//	@ApiOperationSupport(order = 7)
//	@ApiOperation(value = "逻辑删除", notes = "传入ids")
//	public R<Boolean> remove(@ApiParam(value = "主键集合", required = true) @RequestParam List<Long> ids) {
//		modelTableFieldsService.remove(Wrappers.<ModelTableFieldsEntity>lambdaQuery().in(ModelTableFieldsEntity::getModelTableId, ids));
//		return R.status(modelTableService.deleteLogic(ids));
//	}
//
//}

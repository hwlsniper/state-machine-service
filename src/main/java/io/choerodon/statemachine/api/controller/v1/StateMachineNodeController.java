package io.choerodon.statemachine.api.controller.v1;

import io.choerodon.core.base.BaseController;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.statemachine.api.dto.StateMachineNodeDTO;
import io.choerodon.statemachine.api.service.StateMachineNodeService;
import io.choerodon.statemachine.api.validator.StateMachineNodeValidator;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author peng.jiang, dinghuang123@gmail.com
 */
@RestController
@RequestMapping(value = "/v1/organizations/{organization_id}/state_machine_nodes")
public class StateMachineNodeController extends BaseController {

    @Autowired
    private StateMachineNodeService nodeService;

    @Autowired
    private StateMachineNodeValidator nodeValidator;


    @Permission(level = ResourceLevel.ORGANIZATION)
    @ApiOperation(value = "创建节点（草稿）")
    @PostMapping
    public ResponseEntity<List<StateMachineNodeDTO>> create(@PathVariable("organization_id") Long organizationId,
                                                            @RequestParam("stateMachineId") Long stateMachineId,
                                                            @RequestBody StateMachineNodeDTO nodeDTO) {
        nodeValidator.createValidate(nodeDTO);
        return new ResponseEntity<>(nodeService.create(organizationId, stateMachineId, nodeDTO), HttpStatus.CREATED);
    }

    @Permission(level = ResourceLevel.ORGANIZATION)
    @ApiOperation(value = "更新节点（草稿）")
    @PutMapping(value = "/{node_id}")
    public ResponseEntity<List<StateMachineNodeDTO>> update(@PathVariable("organization_id") Long organizationId,
                                                            @PathVariable("node_id") Long nodeId,
                                                            @RequestParam("stateMachineId") Long stateMachineId,
                                                            @RequestBody StateMachineNodeDTO nodeDTO) {
        nodeValidator.updateValidate(nodeDTO);
        return new ResponseEntity<>(nodeService.update(organizationId, stateMachineId, nodeId, nodeDTO), HttpStatus.CREATED);
    }

    @Permission(level = ResourceLevel.ORGANIZATION)
    @ApiOperation(value = "删除节点（草稿）")
    @DeleteMapping(value = "/{node_id}")
    public ResponseEntity<List<StateMachineNodeDTO>> deleteNode(@PathVariable("organization_id") Long organizationId,
                                                                @PathVariable("node_id") Long nodeId,
                                                                @RequestParam("stateMachineId") Long stateMachineId) {
        return new ResponseEntity<>(nodeService.delete(organizationId, stateMachineId, nodeId), HttpStatus.OK);
    }

    @Permission(level = ResourceLevel.ORGANIZATION)
    @ApiOperation(value = "校验是否能删除节点（草稿）")
    @GetMapping(value = "/check_delete/{node_id}")
    public ResponseEntity<Map<String, Object>> checkDelete(@PathVariable("organization_id") Long organizationId,
                                                           @PathVariable("node_id") Long nodeId,
                                                           @RequestParam("stateMachineId") Long stateMachineId) {
        return new ResponseEntity<>(nodeService.checkDelete(organizationId, stateMachineId, nodeId), HttpStatus.OK);
    }

    @Permission(level = ResourceLevel.ORGANIZATION)
    @ApiOperation(value = "根据id获取节点（草稿）")
    @GetMapping(value = "/{node_id}")
    public ResponseEntity<StateMachineNodeDTO> queryById(@PathVariable("organization_id") Long organizationId,
                                                         @PathVariable("node_id") Long nodeId) {
        return new ResponseEntity<>(nodeService.queryById(organizationId, nodeId), HttpStatus.OK);
    }

}

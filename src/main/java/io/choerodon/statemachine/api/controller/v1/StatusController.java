package io.choerodon.statemachine.api.controller.v1;

import io.choerodon.core.base.BaseController;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.statemachine.api.dto.StatusDTO;
import io.choerodon.statemachine.api.service.StatusService;
import io.choerodon.statemachine.api.validator.StateValidator;
import io.choerodon.swagger.annotation.CustomPageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author peng.jiang@hand-china.com
 */
@RestController
@RequestMapping(value = "/v1/organizations/{organization_id}/status")
public class StatusController extends BaseController {

    @Autowired
    private StatusService statusService;

    @Autowired
    private StateValidator stateValidator;

    @Permission(level = ResourceLevel.ORGANIZATION)
    @ApiOperation(value = "分页查询状态列表")
    @CustomPageRequest
    @GetMapping
    public ResponseEntity<Page<StatusDTO>> pagingQuery(@ApiIgnore
                                                       @SortDefault(value = "id", direction = Sort.Direction.DESC) PageRequest pageRequest,
                                                       @PathVariable("organization_id") Long organizationId,
                                                       @RequestParam(required = false) String name,
                                                       @RequestParam(required = false) String description,
                                                       @RequestParam(required = false) String type,
                                                       @RequestParam(required = false) String[] param) {
        return new ResponseEntity<>(statusService.pageQuery(pageRequest, new StatusDTO(name, description, type, organizationId),
                Arrays.stream(param).collect(Collectors.joining(","))), HttpStatus.OK);
    }

    @Permission(level = ResourceLevel.ORGANIZATION)
    @ApiOperation(value = "创建状态")
    @PostMapping
    public ResponseEntity<StatusDTO> create(@PathVariable("organization_id") Long organizationId, @RequestBody StatusDTO statusDTO) {
        stateValidator.validate(statusDTO);
        return new ResponseEntity<>(statusService.create(organizationId, statusDTO), HttpStatus.CREATED);
    }

    @Permission(level = ResourceLevel.ORGANIZATION)
    @ApiOperation(value = "更新状态")
    @PutMapping(value = "/{state_id}")
    public ResponseEntity<StatusDTO> update(@PathVariable("organization_id") Long organizationId,
                                            @PathVariable("state_id") Long stateId,
                                            @RequestBody @Valid StatusDTO statusDTO) {
        statusDTO.setId(stateId);
        statusDTO.setOrganizationId(organizationId);
        stateValidator.validate(statusDTO);
        return new ResponseEntity<>(statusService.update(statusDTO), HttpStatus.CREATED);
    }

    @Permission(level = ResourceLevel.ORGANIZATION)
    @ApiOperation(value = "删除状态")
    @DeleteMapping(value = "/{state_id}")
    public ResponseEntity<Boolean> delete(@PathVariable("organization_id") Long organizationId,
                                          @PathVariable("state_id") Long stateId) {
        return new ResponseEntity<>(statusService.delete(organizationId, stateId), HttpStatus.NO_CONTENT);
    }

    @Permission(level = ResourceLevel.ORGANIZATION)
    @ApiOperation(value = "根据id查询状态对象")
    @GetMapping(value = "/{state_id}")
    public ResponseEntity<StatusDTO> queryStateById(@PathVariable("organization_id") Long organizationId, @PathVariable("state_id") Long statusId) {
        return new ResponseEntity<>(statusService.queryStateById(organizationId, statusId), HttpStatus.OK);
    }

    @Permission(level = ResourceLevel.ORGANIZATION)
    @ApiOperation(value = "查询组织下的所有状态")
    @GetMapping(value = "/selectAll")
    public ResponseEntity<List<StatusDTO>> queryAllState(@PathVariable("organization_id") Long organizationId) {
        return new ResponseEntity<>(statusService.queryAllState(organizationId), HttpStatus.OK);
    }

    @Permission(level = ResourceLevel.ORGANIZATION)
    @ApiOperation(value = "校验状态名字是否未被使用")
    @GetMapping(value = "/check_name")
    public ResponseEntity<Boolean> checkName(@PathVariable("organization_id") Long organizationId,
                                             @RequestParam(value = "state_id", required = false) Long statusId,
                                             @RequestParam("name") String name) {
        return new ResponseEntity<>(statusService.checkName(organizationId, statusId, name), HttpStatus.OK);
    }

}
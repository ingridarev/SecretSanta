package Secret.Santa.Secret.Santa.controllers;


import Secret.Santa.Secret.Santa.models.DTO.GroupDTO;
import Secret.Santa.Secret.Santa.models.Group;
import Secret.Santa.Secret.Santa.models.User;
import Secret.Santa.Secret.Santa.services.IGroupService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static Secret.Santa.Secret.Santa.mappers.GroupMapper.toGroupDTO;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/groups")
public class GroupController {

    private static final Logger logger = LoggerFactory.getLogger(GroupController.class);
    @Autowired
    private IGroupService iGroupService;

    @GetMapping
    public ResponseEntity<List<Group>> getAllGroups() {
        try {
            List<Group> groups = iGroupService.getAllGroups();
            return new ResponseEntity<>(groups, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Failed to get all groups", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<Group> getGroupById(@PathVariable int groupId) {
        try {
            Group group = iGroupService.getGroupById(groupId);
            return ResponseEntity.ok(group);
        } catch (Exception e) {
            logger.error("Failed to get group with ID: {}", groupId, e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Group> createGroup(@Valid @RequestBody GroupDTO groupDTO) {
        try {
            Group group = iGroupService.createGroup(groupDTO);
            return new ResponseEntity<>(group, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Failed to create group", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{groupId}")
    public ResponseEntity<GroupDTO> updateGroup(@PathVariable int groupId, @Valid @RequestBody GroupDTO groupDTO) {
        try {
            GroupDTO group = iGroupService.editByGroupId(groupDTO, groupId);
            return ResponseEntity.ok(group);
        } catch (Exception e) {
            logger.error("Failed to update group with ID: {}", groupId, e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/{userId}/groups")
    public List<Group> getAllGroupsForUser(@PathVariable("userId") Integer userId) {
        try {
            return iGroupService.getAllGroupsForUser(userId);
        } catch (Exception e) {
            logger.error("Error retrieving groups for user with ID: {}", userId, e);
            throw e;
        }
    }

    @GetMapping("/owner/{userId}/groups")
    public List<Group> getAllGroupsForOwner(@PathVariable("userId") Integer userId) {
        try {
            return iGroupService.getAllGroupsForOwner(userId);
        } catch (Exception e) {
            logger.error("Error retrieving groups for owner with ID: {}", userId, e);
            throw e;
        }
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> deleteGroup(@PathVariable int groupId) {
        try {
            boolean deleted = iGroupService.deleteGroupByGroupId(groupId);
            if (deleted) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Failed to delete group with ID: {}", groupId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{groupId}/users/{userId}/newUsers")
    public ResponseEntity<GroupDTO> addUserToGroup(@PathVariable int groupId, @Valid @PathVariable int userId) {

        var updatedGroup = iGroupService.addUserToGroup(groupId, userId);

        return ok(toGroupDTO(updatedGroup));
    }

    @GetMapping(value = "/{groupId}/users")
    @ResponseBody
    public List<User> getAllUsersById(@PathVariable int groupId) {
        return iGroupService.getAllUsersById(groupId);
    }

}

package com.essers.scanning.data.service;

import com.essers.scanning.data.model.Movement;
import com.essers.scanning.data.model.User;
import com.essers.scanning.data.repository.MovementRepository;
import com.essers.scanning.security.SecurityService;
import com.vaadin.flow.component.UI;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.Date;

import static com.essers.scanning.data.constants.Constants.*;

@Service
public final class MovementService {
    private final MovementRepository movementRepository;
    private final SecurityService securityService;

    public MovementService(MovementRepository movementRepository, SecurityService securityService) {
        this.movementRepository = movementRepository;
        this.securityService = securityService;
    }

    //on logout/revisit portal page
    public void abandonMovement() {
        //find current assigned movement
        Movement movement = getAssignedMovement();
        //if movement is null, do nothing
        if (movement == null) {
            return;
        }
        //if movement is not null, set status to to-do and set user and in_progress_timestamp to null
        movement.setStatus(MOVEMENT_STATUS_TODO);
        movement.setUser(null);
        movement.setInProgressTimestamp(null);
        //save movement
        movementRepository.save(movement);
    }

    @Nullable
    public void assignUserToMovement() {
        //get logged in user
        User user = securityService.getAuthenticatedUser();
        Movement movement =
                movementRepository.findNextTodoTaskForActiveCompany(user.getMovementType().getId(),
                        user.getCompany().getId());
        /*
        if movement is not null,
            set status to in progress
            and set user to logged in user
            and in_progress_timestamp to current timestamp
            else redirect to scanning page
       */
        if (movement != null) {

            movement.setUser(user);
            movement.setStatus(MOVEMENT_STATUS_IN_PROGRESS);
            movement.setInProgressTimestamp(new Date());
            //save movement
            movementRepository.save(movement);
        }
        //redirect to scanning page
        UI.getCurrent().getPage().setLocation("/scanning");
    }

    @Nullable
    public Movement getAssignedMovement() {
        //get logged in user
        User user = securityService.getAuthenticatedUser();
        //find movement by user and status in progress
        return movementRepository.findFirstByUserAndStatus(user, MOVEMENT_STATUS_IN_PROGRESS);
    }

    //hande scan submit
    public void handleSubmit(Movement movement) {
        //set status to done and save movement
        movement.setStatus(MOVEMENT_STATUS_DONE);
        movementRepository.save(movement);
        //assign next movement to user
        assignUserToMovement();
    }
}

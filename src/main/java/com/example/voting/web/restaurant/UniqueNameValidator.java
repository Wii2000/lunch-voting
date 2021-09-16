package com.example.voting.web.restaurant;

import com.example.voting.model.NamedEntity;
import com.example.voting.repository.RestaurantRepository;
import com.example.voting.web.GlobalExceptionHandler;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;

@Component
@AllArgsConstructor
public class UniqueNameValidator implements org.springframework.validation.Validator {

    private final RestaurantRepository repository;
    private final HttpServletRequest request;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return NamedEntity.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        NamedEntity restaurant = ((NamedEntity) target);
        if (StringUtils.hasText(restaurant.getName())) {
            repository.findByNameIgnoreCase(restaurant.getName())
                    .ifPresent(dbRestaurant -> {
                        if (request.getMethod().equals("PUT")) {  // UPDATE
                            int dbId = dbRestaurant.id();

                            // it is ok, if update ourself
                            if (restaurant.getId() != null && dbId == restaurant.id()) return;

                            // Workaround for update with restaurant.id=null in request body
                            // ValidationUtil.assureIdConsistent called after this validation
                            String requestURI = request.getRequestURI();
                            if (requestURI.endsWith("/" + dbId))
                                return;
                        }
                        errors.rejectValue("name", null, GlobalExceptionHandler.EXCEPTION_DUPLICATE_NAME);
                    });
        }
    }
}

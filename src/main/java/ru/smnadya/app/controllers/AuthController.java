package ru.smnadya.app.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.smnadya.app.candidate.Candidate;
import ru.smnadya.app.controllers.enums.Attribute;
import ru.smnadya.app.exceptions.AuthenticationException;
import ru.smnadya.app.exceptions.IllegalEmailException;
import ru.smnadya.app.security.AuthenticationService;

import static ru.smnadya.app.controllers.api.AuthenticationControllerApiPath.ACAP_AUTHENTICATE;
import static ru.smnadya.app.controllers.api.BaseControllerApiPath.BCAP_REDIRECT_HOME;
import static ru.smnadya.app.controllers.api.EventControllerApiPath.ECAP_REDIRECT_EVENTS;

@Controller
@RequestMapping(ACAP_AUTHENTICATE)
public class AuthController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping()
    public String logIn(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        HttpSession session,
                        RedirectAttributes attributes) {
        try {
            Candidate candidate = authenticationService.authenticate(email, password);
            session.setAttribute("candidateId", candidate.getId());
            return ECAP_REDIRECT_EVENTS;
        } catch (IllegalEmailException e) {
            attributes.addFlashAttribute(Attribute.ERROR.getDescription(), e.getMessage());
            return BCAP_REDIRECT_HOME;
        } catch (AuthenticationException e) {
            attributes.addFlashAttribute(Attribute.ERROR.getDescription(), e.getMessage());
            throw new RuntimeException(e);
        }
    }

}

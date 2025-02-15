package ru.smnadya.app.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.smnadya.app.candidate.Candidate;
import ru.smnadya.app.controllers.enums.Attribute;
import ru.smnadya.app.exceptions.IllegalEmailException;
import ru.smnadya.app.security.RegistrationService;

import static ru.smnadya.app.controllers.api.BaseControllerApiPath.BCAP_REDIRECT_HOME;
import static ru.smnadya.app.controllers.api.RegistrationControllerApiPath.RCAP_REGISTER;

@Controller
@RequestMapping(RCAP_REGISTER)
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping()
    public String registerCandidate(@RequestParam("username") String name,
                                    @RequestParam("patronymic") String patronymic,
                                    @RequestParam("surname") String surname,
                                    @RequestParam("email") String email,
                                    @RequestParam("password") String password,
                                    RedirectAttributes attributes) {
        Candidate candidate = new Candidate(name, patronymic, surname, email, password);
        try {
            registrationService.register(candidate);
        } catch (IllegalEmailException e) {
            attributes.addFlashAttribute(Attribute.ERROR.getDescription(), e.getMessage());
            return BCAP_REDIRECT_HOME;
        } catch (Exception e) {
            attributes.addFlashAttribute(Attribute.ERROR.getDescription(), "Что-то пошло не так при регистрации");
            return BCAP_REDIRECT_HOME;
        }

        attributes.addFlashAttribute(Attribute.MESSAGE.getDescription(), "Регистрация прошла успешно, можете выполнить вход в свой аккаунт!");
        return BCAP_REDIRECT_HOME;
    }

}

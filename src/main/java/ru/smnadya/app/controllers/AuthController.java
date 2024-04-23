package ru.smnadya.app.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.smnadya.app.dao.CandidateDAO;
import ru.smnadya.app.dao.exceptions.IllegalEmailException;
import ru.smnadya.app.models.Candidate;
import ru.smnadya.app.security.PasswordHasher;

@Controller
@RequestMapping()
public class AuthController {
    private final CandidateDAO candidateDAO;
    private final PasswordHasher hasher = new PasswordHasher();
    @Autowired
    public AuthController(CandidateDAO candidateDAO) {
        this.candidateDAO = candidateDAO;
    }

    @PostMapping("/register")
    public String registerCandidate(@RequestParam("username") String name,
                                    @RequestParam("patronymic") String patronymic,
                                    @RequestParam("surname") String surname,
                                    @RequestParam("email") String email,
                                    @RequestParam("password") String password,
                                    RedirectAttributes attributes) {
        String hashOfPassword = hasher.hashPassword(password);
        Candidate candidate = new Candidate(name, patronymic, surname,
                email, hashOfPassword);
        try {
            candidateDAO.createCandidate(candidate);
        } catch (IllegalEmailException e) {
            attributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/";
        } catch (Exception e) {
            attributes.addFlashAttribute("error",
                    "Что-то пошло не так при регистрации");
            return "redirect:/";
        }
        attributes.addFlashAttribute("message",
                "Регистрация прошла успешно, " +
                        "можете выполнить вход в свой аккаунт!");

        return "redirect:/";
    }
    @PostMapping("/login")
    public String logIn(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        HttpSession session,
                        RedirectAttributes attributes) {
        try {
            Candidate candidate = candidateDAO.getCandidateBy(email);
            if (candidate.getHashOfPassword().equals(hasher.hashPassword(password))) {
                session.setAttribute("candidateId", candidate.getId());
                return "redirect:/events";
            }
            else {
                attributes.addFlashAttribute("error", "Неверный пароль");
                return "redirect:/";
            }
        } catch (IllegalEmailException e) {
            attributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/";
        }
    }
}

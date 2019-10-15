package com.vicevi.crozproblem.controller;

import com.vicevi.crozproblem.form.JokeForm;
import com.vicevi.crozproblem.model.Joke;
import com.vicevi.crozproblem.repository.CategoryRepository;
import com.vicevi.crozproblem.repository.JokeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class JokeController {

    @Autowired
    JokeRepository jokeRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("/")
    public String listJokes(Model model, @RequestParam(defaultValue = "1") int page) {

        List<Joke> sortedJokes = jokeRepository.findAll().stream()
                .sorted((j2, j1) -> Double.compare(j1.getLikes() - j1.getDislikes(), j2.getLikes() - j2.getDislikes()))
                .collect(Collectors.toList());

        PageRequest pr = new PageRequest(page-1, 3);
        int start = (int) pr.getOffset();
        int end = Math.min((start + pr.getPageSize()), sortedJokes.size());

        if(start == end && start != 0)
            throw new RuntimeException("Requested page contains no jokes.");

        model.addAttribute("rankOffset", start);
        model.addAttribute("jokes", new PageImpl<>(sortedJokes.subList(start, end), pr, sortedJokes.size()));
        return "jokes";
    }

    @PostMapping("/update-likes/{id}")
    public String updateLikeAndDislikeCount(@RequestParam(value = "action") String action, @PathVariable("id") int id,
                                            @RequestParam(defaultValue = "1") int page) {

        Joke joke = jokeRepository.findById(id).get();

        if(action.equals("like")) {
            joke.setLikes(joke.getLikes() + 1);
        } else {
            joke.setDislikes(joke.getDislikes() + 1);
        }

        jokeRepository.save(joke);
        return "redirect:/?page=" + page;
    }

    @GetMapping("/new")
    public String showJokeForm(Model model) {
        model.addAttribute("form", new JokeForm());
        model.addAttribute("categories", categoryRepository.findAll());
        return "form";
    }

    @PostMapping("/add-joke")
    public String addJoke(ModelMap model, @ModelAttribute("form") @Valid JokeForm form, BindingResult result) {

        if(result.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            return "form";
        }

        jokeRepository.save(new Joke(form.getContent(), categoryRepository.findById(form.getCategoryId()).get()));
        return "redirect:/";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        if(isSomeoneLoggedIn())
            throw new RuntimeException("This page can't be accessed when already logged in.");
        return "login";
    }

    @GetMapping("/logout-successful")
    public String logoutAndShowHomePage() {
        return "redirect:/";
    }

    private boolean isSomeoneLoggedIn(){
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetails;
    }
}

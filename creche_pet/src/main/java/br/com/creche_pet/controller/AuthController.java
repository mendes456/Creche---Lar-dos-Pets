package br.com.creche_pet.controller;

import br.com.creche_pet.model.UserModel;
import br.com.creche_pet.dto.UserDto;
import br.com.creche_pet.repository.UserRepository;
import br.com.creche_pet.service.PasswordResetService;
import br.com.creche_pet.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.validation.BindingResult;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Controller
public class AuthController {

  @Autowired
  private PasswordResetService passwordResetService;

  private final UserRepository userRepository;
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;

  public AuthController(UserRepository userRepository,
      UserService userService,
      PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
  }

  // ---------------- LOGIN ----------------
  @GetMapping("/login")
  public String loginForm(Model model) {
    model.addAttribute("user", new UserModel()); // ou UserDto
    return "visual/login"; // seu template Thymeleaf
  }

  @PostMapping("/login")
  public String login(@ModelAttribute("user") UserModel userForm,
      Model model,
      HttpSession session) {

    // Busca o usuário por email
    UserModel user = userRepository.findByEmail(userForm.getEmail()).orElse(null);

    // Verifica se usuário existe e se a senha bate
    if (user == null || !passwordEncoder.matches(userForm.getSenha(), user.getSenha())) {
      model.addAttribute("erro", "Email ou senha incorretos!");
      return "visual/login"; // mantém a página de login
    }

    // Verifica se usuário foi aprovado
    if (!user.isAprovado()) {
      model.addAttribute("erro", "Seu acesso ainda não foi aprovado pelo administrador.");
      return "visual/login";
    }

    // Salva o usuário na sessão
    session.setAttribute("user", user);

    return "redirect:/menuadm";

  }

  // ------------- REGISTRO -------------
  @GetMapping("/cadastro")
  public String showRegistrationForm(Model model) {
    model.addAttribute("user", new UserDto());
    return "visual/cadastro"; // seu template Thymeleaf
  }

  @PostMapping("/register/save")
  public String registration(@Valid @ModelAttribute("user") UserDto userDto,
      BindingResult result,
      Model model) {

    UserModel existing = userService.findUserByEmail(userDto.getEmail());
    if (existing != null) {
      result.rejectValue("email", null, "Já existe uma conta com esse email");
    }

    if (result.hasErrors()) {
      return "visual/cadastro";
    }

    userService.saveUser(userDto);
    return "redirect:/login?success";
  }

  // -------------- LISTA DE USUÁRIOS --------------
  @GetMapping("/users")
  public String listRegisteredUsers(Model model) {
    List<UserDto> users = userService.findAllUsers();
    model.addAttribute("users", users);
    return "users";
  }

  // ----------- LOGOUT ----------------
  @GetMapping("/logout")
  public String logout(HttpSession session) {
    session.invalidate();
    return "redirect:/login?logout";
  }

  // ---------- ESQUECI MINHA SENHA ----------
  // 1️ Mostrar página "Esqueci a senha"
  @GetMapping("/forgot-password")
  public String showForgotPasswordPage() {
    return "forgot-password";
  }

  // 2️ Enviar email com link de redefinição
  @PostMapping("/forgot-password")
  public String forgotPassword(@RequestParam String email, Model model) {

    try {
    	   String token = passwordResetService.createPasswordResetToken(email);
    	      String link = "http://localhost:8080/reset-password?token=" + token;
    	      model.addAttribute("link", link); // Adiciona o link no modelo
    	      model.addAttribute("message", "O link de redefinição foi gerado com sucesso.");
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }

    return "forgot-password";
  }

  // 3 Mostrar página para redefinir a senha
  @GetMapping("/reset-password")
  public String showResetForm(@RequestParam String token, Model model) {
    model.addAttribute("token", token);
    return "reset-password";
  }

  // 4️ Processar redefinição da senha
  @PostMapping("/reset-password")
  public String resetPassword(
      @RequestParam String token,
      @RequestParam String password,
      @RequestParam String confirmPassword,
      Model model) {

    // validar senhas iguais
    if (!password.equals(confirmPassword)) {
      model.addAttribute("error", "As senhas não coincidem");
      model.addAttribute("token", token);
      return "reset-password";
    }

    try {
      passwordResetService.resetPassword(token, password);
      model.addAttribute("message", "Senha redefinida com sucesso!");
      return "visual/login"; 
      
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      model.addAttribute("token", token);
      return "reset-password";
    }
  }
}



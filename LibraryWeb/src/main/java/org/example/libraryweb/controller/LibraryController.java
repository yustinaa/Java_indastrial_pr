package org.example.libraryweb.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.libraryweb.model.Book;
import org.example.libraryweb.service.CustomUserDetailsService;
import org.example.libraryweb.service.XMLManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class LibraryController {

    private final XMLManager xmlManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    public LibraryController(XMLManager xmlManager) {
        this.xmlManager = xmlManager;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/reader";
    }

    @GetMapping("/success")
    public String redirectAfterLogin(Authentication authentication) {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if (roles.contains("ROLE_LIBRARIAN")) {
            return "redirect:/librarian";
        } else if (roles.contains("ROLE_READER")) {
            return "redirect:/reader";
        }
        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping("/process_register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String role,
                               HttpServletRequest request) throws Exception {
        xmlManager.registerUser(username, password, role);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        return "redirect:/success";
    }


//библиотекарь

    @GetMapping("/librarian")
    public String librarianMain(Model model) throws Exception {
        model.addAttribute("books", xmlManager.getBooksXPath());
        model.addAttribute("readers", xmlManager.getAllReaders()); // Передаем список читателей
        return "librarian_main";
    }

    @PostMapping("/librarian/give")
    public String giveBook(@RequestParam int id, @RequestParam String readerName) throws Exception {
        xmlManager.giveBookToUser(id, readerName);
        return "redirect:/librarian";
    }

    @GetMapping("/librarian/users")
    public String viewAllUsers(Model model) throws Exception {
        List<String> readers = xmlManager.getAllReaders();
        // Создадим карту: Имя пользователя -> Список его книг
        Map<String, List<Book>> userBooksMap = new HashMap<>();
        for (String reader : readers) {
            List<String> ids = xmlManager.getBorrowedBookIds(reader);
            userBooksMap.put(reader, xmlManager.getBooksByIds(ids));
        }
        model.addAttribute("userBooksMap", userBooksMap);
        return "all_users";
    }

    @PostMapping("/librarian/add")
    public String addBook(@RequestParam String title,
                          @RequestParam String author,
                          @RequestParam int year,
                          @RequestParam double price,
                          @RequestParam String category,
                          @RequestParam int copies) throws Exception {

        int newId = xmlManager.getNewBookId();
        Book book = new Book(newId, copies, copies, title, author, year, price, category);
        xmlManager.addBook(book);

        return "redirect:/librarian";
    }

    @PostMapping("/librarian/change-price")
    public String changePrice(@RequestParam int id, @RequestParam double newPrice) throws Exception {
        xmlManager.changePrice(id, newPrice);
        return "redirect:/librarian";
    }


    //читатель

    @GetMapping("/reader")
    public String readerMain(Model model) {
        model.addAttribute("books", xmlManager.getBooksXPath());
        return "reader_main";
    }

    @GetMapping("/reader/search")
    public String searchBooks(@RequestParam(required = false) String author,
                              @RequestParam(required = false) Integer year,
                              @RequestParam(required = false) String category,
                              Model model) {
        List<Book> filteredBooks = xmlManager.searchBooks(author, year, category);
        model.addAttribute("books", filteredBooks);
        return "reader_main";
    }

    @GetMapping("/reader/my-account")
    public String viewMyAccount(Model model) throws Exception {
        // Получаем имя текущего пользователя из сессии
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Получаем ID его книг и превращаем их в объекты Book
        List<String> borrowedIds = xmlManager.getBorrowedBookIds(username);
        List<Book> myBooks = xmlManager.getBooksByIds(borrowedIds);

        model.addAttribute("username", username);
        model.addAttribute("myBooks", myBooks);
        return "my_account";
    }



}
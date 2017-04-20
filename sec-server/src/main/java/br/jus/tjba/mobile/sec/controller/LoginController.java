package br.jus.tjba.mobile.sec.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import br.jus.tjba.mobile.sec.model.Session;
import br.jus.tjba.mobile.sec.service.CreateSessionService;
import br.jus.tjba.mobile.sec.service.EncodeResponseService;

import static org.springframework.http.MediaType.*;

/**
 * Created by rudolfoborges on 19/04/17.
 */
@RestController
@RequestMapping(value = "v1/login",
        consumes = TEXT_PLAIN_VALUE,
        produces = TEXT_PLAIN_VALUE)
public class LoginController {

    @Autowired
    private CreateSessionService createSessionService;

    @Autowired
    private EncodeResponseService encodeResponseService;

    @PostMapping
    public ResponseEntity<String> login(HttpServletRequest request){
        final Session newSession = createSessionService.createNewSession(request.getRemoteAddr());
        final String json = encodeResponseService.encode(newSession);
        return ResponseEntity.ok(json);
    }

}

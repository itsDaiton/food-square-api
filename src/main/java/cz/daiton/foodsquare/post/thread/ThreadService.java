package cz.daiton.foodsquare.post.thread;

import cz.daiton.foodsquare.security.IncorrectUserException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ThreadService {

    Thread get(Long id);

    List<Thread> getAll();

    Thread add(ThreadDto threadDto);

    String update(ThreadDto threadDto, Long id, HttpServletRequest request) throws IncorrectUserException;

}

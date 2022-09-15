package cz.daiton.foodsquare.appuser;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;

    public AppUserServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public AppUser get(Long id) {
        return appUserRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<AppUser> getAll() {
        return appUserRepository.findAll();
    }

    @Override
    public void add(AppUser appUser) {
        appUserRepository.save(appUser);
    }

    @Override
    public void update(AppUserDto appUserDto, Long id) {
        AppUser appUser = appUserRepository.findById(id).orElseThrow(NoSuchElementException::new);
        appUser.setEmail(appUserDto.getEmail());
        appUser.setUserName(appUserDto.getUserName());
        appUserRepository.save(appUser);
    }

    @Override
    public void delete(Long id) {
        appUserRepository.deleteById(id);
    }

    @Override
    public AppUser findByUsername(String username) {
        return appUserRepository.findByUserName(username).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public Boolean existsByUserName(String username) {
        return appUserRepository.existsByUserName(username);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return appUserRepository.existsByEmail(email);
    }
}

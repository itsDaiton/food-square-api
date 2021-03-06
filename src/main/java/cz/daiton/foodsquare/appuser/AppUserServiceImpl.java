package cz.daiton.foodsquare.appuser;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

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
}

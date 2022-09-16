package cz.daiton.foodsquare.appuser;

import java.util.List;

public interface AppUserService {

    AppUser get(Long id);

    List<AppUser> getAll();

    void add(AppUser appUser);

    void update(AppUserDto appUserDto, Long id);

    void delete(Long id);

    AppUser findByUsername(String username);

    Boolean existsByUserName(String username);

    Boolean existsByEmail(String email);

}

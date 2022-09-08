package cz.daiton.foodsquare.appuser;

import java.util.List;
import java.util.Optional;

public interface AppUserService {

    AppUser get(Long id);

    List<AppUser> getAll();

    void add(AppUser appUser);

    void update(AppUserDto appUserDto, Long id);

    void delete(Long id);

}

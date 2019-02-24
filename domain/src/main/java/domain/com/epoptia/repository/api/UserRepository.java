package domain.com.epoptia.repository.api;

import domain.com.epoptia.model.dto.post.LoginAdminPostDto;
import domain.com.epoptia.model.dto.result.UserDto;
import io.reactivex.Single;

public interface UserRepository {

    Single<UserDto> loginAdmin(String subDomain, LoginAdminPostDto loginAdminPostDto) throws Exception;

}

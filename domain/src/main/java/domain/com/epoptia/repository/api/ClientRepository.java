package domain.com.epoptia.repository.api;

import domain.com.epoptia.model.dto.post.ValidateClientSubDomainPostDto;
import domain.com.epoptia.model.dto.result.ResponseWrapperDto;
import domain.com.epoptia.model.dto.result.UserDto;
import io.reactivex.Single;

public interface ClientRepository {

    Single<UserDto> validateClientSubDomain(String subDomain, ValidateClientSubDomainPostDto validateClientSubDomainDto) throws Exception;

}

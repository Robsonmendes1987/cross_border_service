package com.example.cross_border_service.service.impl;

import com.example.cross_border_service.exception.DuplicateValue;
import com.example.cross_border_service.exception.NotFound;
import com.example.cross_border_service.model.dto.user.RequestUserDTO;
import com.example.cross_border_service.model.dto.user.ResponseUserDTO;
import com.example.cross_border_service.model.entity.UserEntity;
import com.example.cross_border_service.model.repository.UserRepository;
import com.example.cross_border_service.service.UserService;






import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<ResponseUserDTO> getAll() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream().map(ResponseUserDTO::new).collect(Collectors.toList());
    }

    @Override
    public ResponseUserDTO getById(Long id) throws NotFound {
        return new ResponseUserDTO(findById(id));
    }

    @Override
    public List<ResponseUserDTO> getByName(String nome) {
        List<UserEntity> users =   findByName(nome);
        if (users.isEmpty()) throw ( new DuplicateValue("Sinto Muito... Não tenho um usuário cadastrado com esse nome"));
        return users.stream().map(ResponseUserDTO::new).collect(Collectors.toList());
    }

    @Override
    public ResponseUserDTO saveUser(RequestUserDTO user) {
        UserEntity result = user.newUser();
        return new ResponseUserDTO(userRepository.save(result));
    }

    @Override
    public ResponseUserDTO updateUserById(Long id, RequestUserDTO request) {

        UserEntity userEntity = findById(id);
        userEntity.setNome(request.getNome());
        userEntity.setEmail(request.getEmail());
        userEntity.setCpfCnpj(request.getCpfCnpj());
        userRepository.save(userEntity);

        return new ResponseUserDTO(userEntity);
    }

    @Override
    public String deleteUserById(Long id) {
        return null;
    }


    private UserEntity findById(Long id) throws NotFound {
        return userRepository.findById(id).orElseThrow(() -> new NotFound("Usuario não cadastrado com id : " + id));
    }

    private List<UserEntity> findByName(String nome) throws DuplicateValue {
        return userRepository.findByNomeIgnoreCase(nome);
    }

}
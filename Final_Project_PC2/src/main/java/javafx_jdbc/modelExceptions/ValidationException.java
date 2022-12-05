package javafx_jdbc.modelExceptions;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    // Carrega uma coleçao contendo todos os erros
    private Map<String, String> errors = new HashMap<>();

    // Exceção para validação do formulário. Carrego as mensagens de erro do formulário
    public ValidationException(String msg){
        super(msg);
    }

    public Map<String, String> getErros(){
        return errors;
    }

    public void addError(String fieldName, String message){
        errors.put(fieldName, message);
    }

}

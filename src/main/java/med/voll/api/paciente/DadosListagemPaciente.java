package med.voll.api.paciente;

public record DadosListagemPaciente(Long id, String nome, String email, String cpf) { // é um DTO para ser exibido no GET

    public DadosListagemPaciente(Paciente paciente) {
        this(paciente.getId(), paciente.getNome(), paciente.getEmail(), paciente.getCpf());
    }

}

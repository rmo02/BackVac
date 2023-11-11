# API REST para controle de vacinação

Vamos construir uma API REST com Java Spring Boot que irá controlar a aplicação de vacinas entre a população brasileira. 

## Introdução

O primeiro passo vai ser a construção de um cadastro de usuários, onde: nome, e-mail, CPF e data de nascimento, serão obrigatórios e e-mail e CPF serão únicos.

O segundo passo vai ser criar um cadastro de aplicação de vacinas, onde: nome da vacina, e-mail do usuário e a data que foi realizada a vacina serão obrigatórios também. 

Vamos construir apenas dois endpoints neste sistema, o cadastro do usuário e o cadastro da aplicação da vacina. Caso os cadastros estejam corretos vamos retornar o Status 201, caso hajam erros de preenchimento de dados ou campos vazios, o Status será 400.

A arquitetura a ser usada será a REST.

Vamos aplicar no desenvolvimento deste sistema alguns padrões de projeto como: 

1. O padrão Repository para consulta e manipulação dos dados no banco da aplicação.
2. O padrão DTO, onde iremos criar um pacote com classes DTO para realizar as nossas validações através da especificação bean validation em uma camada mais externa, e retornar mensagens de erro mais amigáveis para os consumidores da API.
3. Inversão de controle e injeção de dependência, sendo estes a base do Spring framework.

Iremos usar também o Spring Initializr para iniciar o nosso projeto com mais facilidade e a versão do Java que iremos utilizar será a 14.

Uma possível solução para levantar esta aplicação é através do Hiroku, que pode ser conectado ao repositório do GutHub e configurado um banco de dados PostgreSQL gratuitamente, mas com limite de utilização.

## Dependências do projeto

Primeiramente vamos listar todos as dependências que usaremos no projeto.

1. Spring Web: através desta dependência teremos um servidor autoconfigurável a nossa disposição, pois ele tem o Tomcat incorporado como servidor padrão, com isso nos poupa o trabalho de configurar manualmente.

2. Validation: com esta dependência iremos validar nossos campos como email e cpf nas classes DTO, para que os campos sejam preenchidos corretamente no consumo da API e não tenhamos nenhuma inconsistências nos dados.
3. Spring Data JPA para que possamos persistir e consultar os dados em nosso banco através de anotações ou annotation.
4. Msql Connector Java: usaremos como banco de dados o MySql, então precisaremos desta dependência(driver) para realizar a conexão, no meu caso estou usando o MySql 8.0.22.



## Configuração do banco de dados

Essa é a configuração que será usada no meu caso, se o banco não for criado antes o parâmetro ?createDatabaseIfNotExist=true irá criar um banco com este nome.  

Precisei também usar os parâmetros useTimezone=true&serverTimezone=UTC para configuração de Time Zone.

```
spring.datasource.url=jdbc:mysql://localhost:3306/cadastroVacinas?createDatabaseIfNotExist=true&verifyServerCertificate=false&useSSL=true&useTimezone=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.properties.hibernate.format_sql=true
```

## Iniciando o projeto

Agora que já sabemos quais são as dependências que usaremos no nosso projeto vamos começar criando o pacote onde estarão armazenadas as nossas entidades, darei o nome de "entity".


Dentro do pacote entity vamos criar duas classes: Usuario e Vacina, bem como seus métodos Getters e Setters, construtor padrão e construtor com todos os campos menos id, hashCode e equals, vaja o código abaixo.

```
@Entity
@Table(name = "USUARIOS")
public class Usuario{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String CPF;

    @Column(nullable = false)
    private LocalDate dataNascimento;

    public Usuario() {

    }
    
    //Construtores, hashCode, equals, getters e setters...
```

Anotaremos os atributos das nossa classe Usuario com as anotações do JPA, através dessas anotações iremos configurar a persistência da nossa entidade no banco de dados e para entender bem será necessário algumas considerações.

Através da anotação ```@Entity```  nós definimos a nossa classe como uma entidade de banco de dados onde todos os atributos que não tiverem uma anotação especifica serão persistidos como um campo no banco de dado, já a anotação ```@Table(name = "USUARIOS")```, Indica o nome da tabela do banco de dados onde os campos serão persistidos.

As anotações ```@Id e @GeneratedValue(strategy = GenerationType.IDENTITY) ``` respectivamente, indica que o campo será uma chave primária e que a responsabilidade pela geração dos valores será do provedor de persistência.

A anotação ```@Column(nullable = false, unique = true) ``` com suas respectivas propriedades indicam que as colunas no banco de dados não poderão ser nulas e deverão ser únicas, ou seja, não poderá haver CPF's e email's repedidos.

Repare abaixo que o código da classe Vacina é igual, com exceção do campo email, que leva na anotação ```@Column ``` a propriedade ``` name = "email_usuario" ```, que indica o nome da coluna que será persistida no banco.

```@Entity
@Table(name = "VACINAS")
public class Vacina{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email_usuario", nullable = false)
    private String email;

    @Column(nullable = false)
    private String nomeVacina;

    @Column(nullable = false)
    private LocalDate dataVacinacao;

 //Construtores, hashCode, equals, getters e setters...
 
```

## Construindo nosso Repositório

Nessa parte do projeto, nós iremos crias um pacote chamado "repository", onde colocaremos duas interfaces: "UsuarioRepository" e "VacinaRepository", 

![Passo1](./repository.png "")


Essas interfaces serão responsáveis por herdar outra interface que se chama JpaRepository, da qual nós iremos usar métodos que irão nos ajudar a manipular o banco de dados, e teremos que declarar dois explicitamente que é o  ```findById e deleteById``` para localizar e excluir um usuário respectivamente.

``` 
@Repository
public interface VacinaRepository extends JpaRepository<Vacina,Long> {
    Vacina findById(long id);
    Vacina deleteById(long id);
}

//-------------------------------------------------------------------

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findById(long id);
    Usuario deleteById(long id);
}



```


Repare que depois que estendemos JpaRepository, temos uma generic ```<Usuario, Long>```, nós temos que colocar qual entidade iremos manipular e qual o "tipo" que foi declarado para o id dentro da classe, nesse caso Usuario.

## Usando boas práticas com a classes Serviços

O modelo MVC, propõe a construção do sistema dividido em Model, View e Controller, mas quando chaga a hora da realizar a manipulação dos dados e a lógica de negócio, muitas pessoas tem dificuldades de estruturar as camadas, a forma correta de implementar assa arquitetura é usar uma classe de serviço usando a anotação ```@Service``` na sua declaração, dentro dessa classe é o lugar certo para invocar os métodos de persistência do repository, muitas desenvolvedores, principalmente iniciantes, chamam esses métodos dentro da controller o que não é uma boa prática, na classe Service são criados métodos que chamam métodos de consulta e persistência do repository e por sua vez são são implementados dentro da controller, onde serão chamados pelas requisições http.
Veja abaixo como iremos implementar as classes Service's e Controller's.


## Resposta para remoção de Usuário e remoção cadastro de vacinação.

Quando fazemos o teste de requisição do método para deletar uma informação da API no Postman, depois de implementada completamente, o retorno que tive inicialmente foi "nada", então criaremos uma classe no pacote DTO chamada de MensagemRespostaDTO que usaremos para entregar uma resposta de sucesso na hora de deletar um usuário ou um cadastro de vacina do banco de dados.

```
public class MensagemRespostaDTO {

    String mensagem;

    public MensagemRespostaDTO(String deletado) {
        this.mensagem = deletado;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}

```

A implementação desse método estão nas classes de serviço.


## Retornando Código de Status 201 e 400.

Dentro da nossa controller tanto UsuarioController quanto VacinaController, iremos implementar os métodos HttpStatus e ResponseEntity, esses métodos irão retornar para nós os códigos de status 201(CREATED) se o usuário for inserido com sucesso no banco de dados, e caso haja algum erro nos campos ou se o mesmo for deixado em branco, irá nos retornar o código de status 400(BAD REQUEST).

```
    @PostMapping
    public ResponseEntity<Usuario> criarUsuario(@RequestBody @Valid UsuarioDTO usuarioDTO){
         Usuario usuarioSalvo =  usuarioService.salvar(usuarioDTO.dtoParaUsuario());
         if (usuarioDTO.equals(null)){
             return new ResponseEntity<>(usuarioSalvo, HttpStatus.BAD_REQUEST);
         }
         return new ResponseEntity<>(usuarioSalvo, HttpStatus.CREATED);
    }
    
```

Repare que existe uma anotação ```@Valid``` Antes de UsuarioDTO, ela serve para validar os dados dos nossos campos da classe Usuario, assunto que falaremos no próximo tópico.
 
## Implementando as classes DTO
Através das classe DTO iremos realizar nossas validações.
O padrão DTO serve para blindar nossas aplicações, com ele podemos impedir que um usuário mal intencionado coloque a si mesmo como administrador de sistemas por exemplo, um outro exemplo é que podemos também ao retornar uma resposta de persistência, omitirmos campos como senhas ou outras informações que julgarmos confidencial, não é o nosso caso pois a aplicação que estamos desenvolvendo não tem como requisito esse recurso.

Confira a implementação da classe UsuarioDTO e VacinaDTO.

A primeira anotação ``` @NotBlank ``` restringe que o campo fique em branco, enquanto a anotação ``` @Size(max = 200)``` coloca um limite máximo de 200 caracteres no campo a ser inserido.

A anotação ``` @NotNull``` é parecida com a ```@NotBlank ``` com a diferença que esta os espaços em branco não são contados como verificação do tamanho do valor.

Já as anotações ```@Email e @CPF ``` validam se o que foi digitado é de fato um email ou cpf válido.

A implementação da classe VacinaDTO é parecida.

Dentro das Classes VacinaDTO e UsuarioDTO devem ser implementados os seguintes métodos.

```
// Para Usuario DTO

 public Usuario dtoParaUsuario(){
        return new Usuario(nome, email, CPF, dataNascimento);
    }
    
    
// Para VacinaDTO

public Vacina dtoParaVacina(){
        return new Vacina(nomeVacina, dataVacinacao,  email);
    }
```

Esses métodos transformam o DTO em objetos do tipo Vacina e Usuario respectivamente.
Se olharmos dentro das controller's veremor que o objeto que está recebendo a nosso requisição, na verdade são do tipo DTO, tanto na VacinaController quanto no UsuarioController, aí que acontece toda a nossa validação, mas na hora de salvar no banco de dados essas informações são transformadas e  transferidas para o método serviço que realiza a persistência no banco de dados. 

## Implementação do sistema na web

Para implementar esse sistema na web, primeiro temos que levantar em um servidor, teremos no entanto que criar um arquivo no diretório raiz do nosso projeto chamado system.properties e colocar nele a especificação da versão do Java que estamos utilizando.

```java.runtime.version=14 
```

Com isso podemos levantar nossa aplicação no Hiroku através da conexão do nosso repositório do GitHub depois de configurar o banco de dados no servidor.

## Consumo da API

### Endpoint /api/v1/usuario/

1. Via requisição POST

		http://localhost:8080/api/v1/usuario/
		ou http://endereçodoservidor/api/v1/usuario/
		
		Cria um novo usuário no banco de dados
		
2. Via requisição GET

	http://localhost:8080/api/v1/usuario/
	ou http://endereçodoservidor/api/v1/usuario/
		
	Consulta todos os usuários do banco.

3.Via requisição GET

	http://localhost:8080/api/v1/usuario/id
	ou http://endereçodoservidor/api/v1/usuario/id
	
	Consulta usuário por id (Substituir id pelo id do usuário).

4. Via requisição DELETE

	http://localhost:8080/api/v1/usuario/id
	ou http://endereçodoservidor/api/v1/usuario/id
	
	Remove usuário por id (Substituir id pelo id do usuário).

5. Via requisição PUT

	http://localhost:8080/api/v1/usuario/id
	ou http://endereçodoservidor/api/v1/usuario/id
	
	Atualiza usuário por id (Substituir id pelo id do usuário).

### Endpoint - /api/v1/vacina/

1. Via requisição POST

http://localhost:8080/api/v1/vacina/
ou http://endereçodoservidor/api/v1/vacina/

Cria um novo cadastro de vacinação no banco de dados

2. Via requisição GET

http://localhost:8080/api/v1/vacina/
ou http://endereçodoservidor/api/v1/vacina/

Consulta todos os cadastros de vacinação do banco de dados.

3. Via requisição GET

http://localhost:8080/api/v1/vacina/id
ou http://endereçodoservidor/api/v1/vacina/id

Consulta cadastro de vacinação por id (Substituir id pelo id do cadastro de vacinação).

4. Via requisição DELETE

http://localhost:8080/api/v1/vacina/id
ou http://endereçodoservidor/api/v1/vacina/id

Remove cadastro de vacinação por id (Substituir id pelo id do cadastro de vacinação).

OBS: Não vi sentido de atualizar um cadastro de vacinação, visto que cada vacina é única e imutável, se houver erros no cadastro da mesma a opção viável será remover o cadastro e refazer da forma correta.

# Informações sobre o Código

O sistema foi feito em java e foi utilizado o SpringBoot, SpringMVC e Thymeleaf.

Os arquivos java estão divididos em 3 partes:

1. entity:
  * Diretório reservado para as classes de entidades "Lap" e "Pilot".
2. service:
  * Diretório reservado para as classes de serviços e lógica de negócio do sistema.
3. controller:
  * Diretório reservado para classes de controle e endpoints.


# Informações sobre o Sistema

Sistema para classificação e resultados de uma corrida, através de um arquivo txt.

## Como funciona?

Na página principal, busque e envie um arquivo .txt com informações sobre cada volta da corrida separados por vírgula:

| Hora da Volta | Nº Piloto - Nome Piloto | Nº Volta | Tempo Volta | Velocidade Média |
|---------------|-------------------------|----------|-------------|------------------|
|23:49:08.277   | 038 – F.MASSA           | 1        | 1:02.852    | 44,275           |

![Alt text](/src/main/resources/static/image/race1.png?raw=true)

## Resultado

O sistema retornará uma tabela conforme imagem abaixo.

![Alt text](/src/main/resources/static/image/race2.png?raw=true)

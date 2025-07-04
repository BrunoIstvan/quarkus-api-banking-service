create table if not exists endereco(
    id serial primary key,
    tipo_endereco text not null,
    logradouro text not null,
    complemento text null,
    numero text null,
    bairro text not null
);

create table if not exists agencia(
    id serial primary key,
    nome text not null,
    razao_social text not null,
    cnpj text not null,
    endereco_id serial references endereco
);
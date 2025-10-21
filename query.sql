create table tb_clients (
    id bigint not null,
    name varchar(100) not null,
    cpf bigint not null,
    fone bigint,
    address text,
    number integer,
    city varchar(100),
    state varchar(50),
    creation_date timestamp default current_timestamp,
	constraint pk_id_clients primary key (id),
	constraint uq_cpf_clients unique (cpf)
);

create table tb_products (
	id bigint not null,
	code varchar(11) not null,
	name varchar(50),
	description text,
	price numeric(10, 2) not null,
	category varchar(50),
	constraint pk_id_products primary key (id),
	constraint uq_code_products unique (code)
);

create table tb_sales(
	id bigint not null,
	code varchar(11) not null,
	id_client_fk bigint not null,
	total_price numeric(10, 2) not null,
	date timestamp not null,
	status varchar(50) not null,
	constraint pk_id_sales primary key (id),
	constraint uq_code_sale unique (code),
	constraint fk_id_client_sale foreign key (id_client_fk) references tb_clients(id)
);

create table tb_product_quantity(
    id bigint not null,
    id_product_fk bigint not null,
    id_sale_fk bigint not null,
    quantity int not null,
    total_price numeric(10,2) not null,
    constraint pk_id_product_qty primary key(id),
    constraint fk_id_product_qty foreign key (id_product_fk) references tb_products(id),
    constraint fk_id_product_sale foreign key (id_sale_fk) references tb_sales(id)
);

create table tb_stock(
	id bigint not null,
	id_product_fk bigint not null,
	quantity integer not null,
	constraint pk_tb_stock primary key (id),
	constraint fk_tb_stock_product foreign key (id_product_fk) references tb_products (id) on delete cascade
);

create sequence sq_clients
start 1
increment 1
owned by tb_clients.id;

create sequence sq_product_quantity
start 1
increment 1
owned by tb_product_quantity.id;

create sequence sq_products
start 1
increment 1
owned by tb_products.id;

create sequence sq_sales
start 1
increment 1
owned by tb_sales.id;

create sequence sq_stock
start 1
increment 1
owned by tb_stock.id;
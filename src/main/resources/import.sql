--SET IDENTITY_INSERT fabricante ON
insert into fabricante (id, nome) values (1, 'VOLKSWAGEN');
insert into fabricante (id, nome) values (2, 'GM');
--SET IDENTITY_INSERT fabricante OFF

insert into produto (nome, preco, fabricante_id) values ('FUSCA', 2000.07, 1);
insert into produto (nome, preco, fabricante_id) values ('OPALA', 3000.40, 2);
insert into produto (nome, preco, fabricante_id) values ('CHEVETE', 2500.54,2);
insert into produto (nome, preco, fabricante_id) values ('OMEGA', 5000.63,2);
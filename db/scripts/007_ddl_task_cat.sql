CREATE TABLE task_cat(
   id serial PRIMARY KEY,
   task_id int not null REFERENCES tasks(id),
   cat_id int not null REFERENCES categories(id)
);
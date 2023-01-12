CREATE TABLE TASK_CATEGORIES(
    category_id NUMBER,
    category_name VARCHAR2(100),
    category_description VARCHAR2(500),
    PRIMARY KEY (category_id)
);

CREATE TABLE TASKS(
    task_id NUMBER,
    task_name VARCHAR2(100),
    task_description VARCHAR2(500),
    deadline TIMESTAMP,
    category_id NUMBER,
    PRIMARY KEY (task_id),
    FOREIGN KEY (category_id) references TASK_CATEGORIES(category_id)
);
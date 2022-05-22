--liquibase formatted sql

--changeset balanton:1

CREATE TABLE IF NOT EXISTS public.teachers
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    name character varying COLLATE pg_catalog."default" UNIQUE NOT NULL,
    CONSTRAINT teacher_id PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.teachers
    OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.students
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    name character varying COLLATE pg_catalog."default" UNIQUE NOT NULL,
    CONSTRAINT student_id PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.students
    OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.courses
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    name character varying COLLATE pg_catalog."default" UNIQUE NOT NULL,
    max_grade integer DEFAULT 0,
    teacher_id integer NOT NULL,
    CONSTRAINT course_id PRIMARY KEY (id),
    CONSTRAINT teacher_id_fkey FOREIGN KEY (teacher_id)
    REFERENCES public.teachers (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.courses
    OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.student_course_relations
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    student_id integer NOT NULL,
    course_id integer NOT NULL,
    CONSTRAINT student_id_fkey FOREIGN KEY (student_id)
    REFERENCES public.students (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE,
    CONSTRAINT course_id_fkey FOREIGN KEY (course_id)
    REFERENCES public.courses (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE,
    UNIQUE (student_id, course_id),
    grade integer DEFAULT 0,
    review character varying DEFAULT ''
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.student_course_relations
    OWNER to postgres;

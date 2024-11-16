# java-filmorate
Template repository for Filmorate project.

![2024-11-16_22-18-28](https://github.com/user-attachments/assets/3e20c3cd-13b4-4af1-92d5-150208dda546)

**Пояснение к схеме:**

Таблица mpa:
- Хранит информацию о возрастных рейтингах фильмов. 
- Атрибуты: id, name.

Таблица films:
- Содержит данные о фильмах, включая их название, описание, продолжительность, год выхода и рейтинг.
- Атрибуты: id, name, description, duration, release_date, mpa_id.
- Связи: mpa_id (Foreign Key) ссылается на mpa(id).

Таблица genres:
- Содержит уникальные жанры фильмов.
- Атрибуты: id, name.

Таблица film_genres:
- Связывает фильмы и жанры (так как у одного фильма может быть несколько жанров).
- Атрибуты: film_id, genre_id.
- Связи: film_id (Foreign Key) ссылается на films(id), genre_id (Foreign Key) ссылается на genre(id).

Таблица users:
- Хранит информацию о пользователях.
- Атрибуты: id, email, login, name, birthday.

Таблица film_likes:
- Связывает фильмы и пользователей, показывая, какие фильмы понравились каким пользователям.
- Атрибуты: film_id, user_id.
- Связи: film_id (Foreign Key) ссылается на films(id), user_id (Foreign Key) ссылается на users(id).

Таблица friendships:
- Хранит информацию о дружеских связях между пользователями и статусе этих связей.
- Атрибуты: user_id, friend_id, status.
- Связи: user_id (Foreign Key) ссылается на users(id), friend_id (Foreign Key) ссылается на users(id).



 **Примеры запросов для основных операций приложения:**

1. Получение списка всех фильмов
    
        SELECT f.*,
               m.name,
               g.name
        FROM films AS f
        LEFT JOIN mpa AS m ON f.id = m.id
        LEFT JOIN genres AS g ON f.id = g.id;
    
3. Получение информации о фильме по его ID
    
        SELECT f.*,
               m.name,
               g.name
        FROM films AS f
        LEFT JOIN mpa AS m ON f.id = m.id
        LEFT JOIN genres AS g ON f.id = g.id;
        WHERE f.id = ?;
    
4. Получение информации о пользователе по его ID
        
        SELECT u.*
        FROM users AS u
        WHERE u.id = ?;

5. Получение списка друзей пользователя
    
        SELECT u.id,
               u.email,
               u.login,
               u.name,
               u.birthday
        FROM users AS u
        WHERE id IN (SELECT friend_id FROM friends WHERE user_id = ? AND status = 'ACCEPTED');

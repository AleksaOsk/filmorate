# java-filmorate
Template repository for Filmorate project.

![2024-11-16_22-13-43](https://github.com/user-attachments/assets/67da9f35-e9e4-4d99-9bef-bf22ef5974c7)

**Пояснение к схеме:**

Таблица rating:
- Хранит информацию о рейтингах фильмов. 
- Атрибуты: rating_id, name.

Таблица movies:
- Содержит данные о фильмах, включая их название, описание, продолжительность, год выхода и рейтинг.
- Атрибуты: film_id, title, description, duration, release_year, rating_id.
- Связи: rating_id (Foreign Key) ссылается на rating(rating_id).

Таблица category:
- Содержит уникальные жанры фильмов.
- Атрибуты: category_id, name.

Таблица film_category:
- Связывает фильмы и жанры (так как у одного фильма может быть несколько жанров).
- Атрибуты: film_id, category_id.
- Связи: film_id (Foreign Key) ссылается на movies(film_id), category_id (Foreign Key) ссылается на category(category_id).

Таблица users:
- Хранит информацию о пользователях.
- Атрибуты: user_id, email, login, name, birthday.

Таблица film_likes:
- Связывает фильмы и пользователей, показывая, какие фильмы понравились каким пользователям.
- Атрибуты: film_id, user_id.
- Связи: film_id (Foreign Key) ссылается на movies(film_id), user_id (Foreign Key) ссылается на users(user_id).

Таблица friendships:
- Хранит информацию о дружеских связях между пользователями и статусе этих связей.
- Атрибуты: user_id, friend_id, status.
- Связи: user_id (Foreign Key) ссылается на users(user_id), friend_id (Foreign Key) ссылается на users(user_id).



 **Примеры запросов для основных операций приложения:**

1. Получение списка всех фильмов
    
        SELECT f.*,
               r.name,
               c.name
        FROM films AS f
        LEFT JOIN rating AS r ON f.id = r.id
        LEFT JOIN category AS c ON f.id = c.id;
    
3. Получение информации о фильме по его ID
    
        SELECT f.*,
               r.name,
               c.name
        FROM films AS f
        LEFT JOIN rating AS r ON f.id = r.id
        LEFT JOIN category AS c ON f.id = c.id
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

-- Seed data for NAPI EU blog platform

-- Insert default admin user (password: admin123 - CHANGE IN PRODUCTION!)
-- Password is BCrypt hashed
INSERT INTO users (username, password, email, role) VALUES
    ('admin', '$2a$10$rZ7QJRvLhvB5DqP3tI.uO.RkGvY7qC4qNqBvQz3nQZE5HHQv1KQVG', 'admin@napieu.com', 'ADMIN');

-- Insert categories (matching the old napieu.com structure)
INSERT INTO categories (name, slug, description, color, display_order) VALUES
                                                                           ('EU hírek', 'eu-hirek', 'European Union news and updates', '#0052B4', 1),
                                                                           ('Világ', 'vilag', 'World news and international affairs', '#009933', 2),
                                                                           ('Életmód', 'eletmod', 'Lifestyle and culture', '#FF6B6B', 3),
                                                                           ('Kultúra', 'kultura', 'Arts and culture', '#9B59B6', 4),
                                                                           ('Egyéb kategória', 'egyeb-kategoria', 'Other news and topics', '#95A5A6', 5),
                                                                           ('Európa', 'europa', 'European affairs', '#003399', 6),
                                                                           ('USA', 'usa', 'United States news', '#B22234', 7),
                                                                           ('Ukrajna', 'ukrajna', 'Ukraine news and updates', '#005BBB', 8),
                                                                           ('Kína', 'kina', 'China news', '#DE2910', 9),
                                                                           ('Oroszország', 'oroszorszag', 'Russia news', '#D52B1E', 10),
                                                                           ('Közel-Kelet', 'kozel-kelet', 'Middle East news', '#CE1126', 11),
                                                                           ('Afrika', 'afrika', 'African news', '#FCD116', 12),
                                                                           ('Magyarország', 'magyarorszag', 'Hungary news', '#436F4D', 13),
                                                                           ('NATO', 'nato', 'NATO news and defense', '#003D7A', 14),
                                                                           ('Tudomány', 'tudomany', 'Science and technology', '#00A8CC', 15),
                                                                           ('Video', 'video', 'Video content', '#E74C3C', 16);

-- Insert a sample article
INSERT INTO articles (title, slug, content, excerpt, author, read_time, published, published_at) VALUES
    ('Üdvözöljük az új NAPI EU platformon!',
     'udvozoljuk-az-uj-napi-eu-platformon',
     '<p>Üdvözöljük megújult platformunkon! Az új NAPI EU egy biztonságos, modern és felhasználóbarát blogging rendszer, amely lehetővé teszi számunkra, hogy minőségi tartalmat szolgáltassunk az Európai Unióval és a világgal kapcsolatos hírekről.</p><p>Az új platform számos fejlesztést tartalmaz, beleértve a jobb biztonságot, gyorsabb betöltési időt és könnyebb tartalomkezelést.</p>',
     'Ismerje meg az új NAPI EU platformot és annak funkcióit.',
     'NapiEU',
     3,
     true,
     CURRENT_TIMESTAMP);

-- Link the sample article to EU hírek category
INSERT INTO article_categories (article_id, category_id)
SELECT 1, id FROM categories WHERE slug = 'eu-hirek';
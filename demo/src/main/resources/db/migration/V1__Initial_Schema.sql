-- Initial database schema for NAPI EU blog platform

-- Users table (for admin authentication)
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(100),
                       role VARCHAR(20) DEFAULT 'ADMIN',
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Categories table
CREATE TABLE categories (
                            id BIGSERIAL PRIMARY KEY,
                            name VARCHAR(100) NOT NULL,
                            slug VARCHAR(100) UNIQUE NOT NULL,
                            description TEXT,
                            color VARCHAR(7),
                            display_order INT DEFAULT 0,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Articles table
CREATE TABLE articles (
                          id BIGSERIAL PRIMARY KEY,
                          title VARCHAR(500) NOT NULL,
                          slug VARCHAR(500) UNIQUE NOT NULL,
                          content TEXT NOT NULL,
                          excerpt TEXT,
                          featured_image VARCHAR(500),
                          author VARCHAR(100) DEFAULT 'NapiEU',
                          read_time INT,
                          views INT DEFAULT 0,
                          published BOOLEAN DEFAULT FALSE,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          published_at TIMESTAMP,
                          source VARCHAR(200)
);

-- Article-Category junction table (many-to-many)
CREATE TABLE article_categories (
                                    article_id BIGINT REFERENCES articles(id) ON DELETE CASCADE,
                                    category_id BIGINT REFERENCES categories(id) ON DELETE CASCADE,
                                    PRIMARY KEY (article_id, category_id)
);

-- Indexes for performance
CREATE INDEX idx_articles_published ON articles(published, published_at DESC);
CREATE INDEX idx_articles_slug ON articles(slug);
CREATE INDEX idx_categories_slug ON categories(slug);
CREATE INDEX idx_article_categories_article ON article_categories(article_id);
CREATE INDEX idx_article_categories_category ON article_categories(category_id);
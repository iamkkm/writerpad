package com.xebia.fs101.writerpad.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xebia.fs101.writerpad.api.representations.ArticleRequest;
import com.xebia.fs101.writerpad.api.representations.UserRequest;
import com.xebia.fs101.writerpad.domain.Article;
import com.xebia.fs101.writerpad.domain.ArticleStatus;
import com.xebia.fs101.writerpad.domain.User;
import com.xebia.fs101.writerpad.repository.ArticleRepository;
import com.xebia.fs101.writerpad.repository.CommentRepository;
import com.xebia.fs101.writerpad.repository.UserRepository;
import com.xebia.fs101.writerpad.service.EmailService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Arrays;
import java.util.UUID;

import static com.xebia.fs101.writerpad.domain.UserRole.ADMIN;
import static com.xebia.fs101.writerpad.domain.UserRole.EDITOR;
import static com.xebia.fs101.writerpad.domain.UserRole.WRITER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ArticleResourceTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CommentRepository commentRepository;

    private User user;

    @BeforeEach
    void setUp() {
        UserRequest userRequest = new UserRequest.Builder()
                .withUsername("kamal")
                .withEmail("kamal@mail.com")
                .withPassword("abc")
                .withRole(WRITER)
                .build();
        user = userRequest.toUser(passwordEncoder);
        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        commentRepository.deleteAll();
        articleRepository.deleteAll();
        userRepository.deleteAll();;
    }


    @Test
    void mock_mvc_not_null() {
        assertThat(mockMvc).isNotNull();
    }

    @Test
    void check_api_articles_post_request_status_as_created() throws Exception {
        ArticleRequest articleRequest = new ArticleRequest.Builder()
                .withBody("You have to believe")
                .withTitle("How to learn Spring Booot")
                .withDescription("Ever wonder how?").build();
        String mockJson = objectMapper.writeValueAsString(articleRequest);
        mockMvc.perform(post("/api/articles")
                .content(mockJson)
                .with(httpBasic("kamal", "abc"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void should_give_status_bad_request() throws Exception {
        String json = "{\n" +
                "  \"title\": \"\",\n" +
                "  \"description\": \"Ever wonder how?\",\n" +
                "  \"tags\": [\"java\", \"Spring Boot\", \"tutorial\"],\n" +
                "  \"featuredImage\": \"url of the featured image\"\n" +
                "}";
        mockMvc.perform(post("/api/articles")
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
                .with(httpBasic("kamal", "abc"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_update_article() throws Exception {
        ArticleRequest updateRequest = new ArticleRequest.Builder()
                .withBody("Abc")
                .withTitle("Abc")
                .withDescription("Abc")
                .build();
        Article article = new Article.Builder()
                .withTitle("DEF")
                .withDescription("Desc")
                .withBody("DEF")
                .build();
        article.setUser(user);
        Article saved = articleRepository.save(article);
        String json = objectMapper.writeValueAsString(updateRequest);
        String id = String.format("%s-%s", saved.getSlug(), saved.getId());
        this.mockMvc.perform(patch("/api/articles/{slug_uuid}", id)
                .contentType(MediaType.APPLICATION_JSON).content(json)
                .with(httpBasic("kamal", "abc")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Abc"))
                .andExpect(jsonPath("$.updatedAt", CoreMatchers.not(saved.getUpdatedAt())));
    }

    @Test
    void should_delete_an_article() throws Exception {
        Article article = new Article.Builder()
                .withTitle("spring")
                .withBody("appl")
                .withDescription("boot")
                .build();
        UserRequest userRequest = new UserRequest.Builder()
                .withUsername("kamal1")
                .withEmail("kamal1@mail.com")
                .withPassword("abc")
                .withRole(ADMIN)
                .build();
        user = userRequest.toUser(passwordEncoder);
        userRepository.save(user);
        article.setUser(user);
        Article savedArticle = articleRepository.save(article);
        String id = String.format("%s-%s", savedArticle.getSlug(), savedArticle.getId());
        this.mockMvc.perform(delete("/api/articles/{slug_id}", id)
                .with(httpBasic("kamal1", "abc")))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void should_not_delete_an_article() throws Exception {
        String id = "abc" + "-" + UUID.randomUUID().toString();
        UserRequest userRequest = new UserRequest.Builder()
                .withUsername("kamal1")
                .withEmail("kamal1@mail.com")
                .withPassword("abc")
                .withRole(ADMIN)
                .build();
        user = userRequest.toUser(passwordEncoder);
        userRepository.save(user);
        this.mockMvc.perform(delete("/api/articles/{slug_id}", id)
                .with(httpBasic("kamal1", "abc")))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void should_list_all_articles() throws Exception {
        Article article1 = createArticle("Title1", "Description1", "body1");
        Article article2 = createArticle("Title2", "description2", "body2");
        Article article3 = createArticle("Title3", "description3", "body3");
        article1.setUser(user);
        article2.setUser(user);
        article3.setUser(user);
        articleRepository.saveAll(Arrays.asList(article1, article2, article3));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/articles")
                .with(httpBasic("kamal", "abc")))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void should_list_all_articles_with_pagination() throws Exception {
        Article article1 = createArticle("Title1", "Description1", "body1");
        Article article2 = createArticle("Title2", "description2", "body2");
        Article article3 = createArticle("Title3", "description3", "body3");
        article1.setUser(user);
        article2.setUser(user);
        article3.setUser(user);
        articleRepository.saveAll(Arrays.asList(article1, article2, article3));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/articles?pageNo=0&pageSize=1")
                .with(httpBasic("kamal", "abc")))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void should_find_article_by_id() throws Exception {
        Article article = new Article.Builder()
                .withBody("Body")
                .withTitle("Title")
                .withDescription("Desc")
                .build();
        article.setUser(user);
        Article saved = articleRepository.save(article);
        String id = saved.getSlug() + "-" + saved.getId();
        String uri = "/api/articles/" + id;
        mockMvc.perform(get(uri)
                .with(httpBasic("kamal", "abc")))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Title"))
                .andExpect(jsonPath("$.body").value("Body"))
                .andExpect(jsonPath("$.description").value("Desc"));
    }

    @Test
    void should_list_all_articles_by_status_code_and_status_code_is_DRAFT() throws Exception {
        Article article1 = createArticle("Title1", "Desc1", "body1");
        Article article2 = createArticle("Title2", "Desc2", "body2");
        article1.setUser(user);
        article2.setUser(user);
        articleRepository.saveAll(Arrays.asList(article1, article2));
        mockMvc.perform(get("/api/articles?status=DRAFT", "DRAFT")
                .with(httpBasic("kamal", "abc")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void should_return_400_when_status_is_changed_to_published_of_an_already_published_article() throws Exception {
        Article article = createArticle("Title", "Desc", "Body");
        UserRequest userRequest = new UserRequest.Builder()
                .withUsername("kamal1")
                .withEmail("kamal1@mail.com")
                .withPassword("abc")
                .withRole(EDITOR)
                .build();
        User user = userRequest.toUser(passwordEncoder);
        userRepository.save(user);
        article.setStatus(ArticleStatus.PUBLISHED);
        article.setUser(user);
        Article saved = articleRepository.save(article);
        mockMvc.perform(post("/api/articles/{sluguuid}/PUBLISH", "Title-" + saved.getId())
                .with(httpBasic("kamal1", "abc")))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_be_able_to_publish_the_article() throws Exception {
        Article article = createArticle("Title", "Desc", "body");
        UserRequest userRequest = new UserRequest.Builder()
                .withUsername("kamal1")
                .withEmail("kamal1@mail.com")
                .withPassword("abc")
                .withRole(EDITOR)
                .build();
        User user = userRequest.toUser(passwordEncoder);
        userRepository.save(user);
        article.setUser(user);
        Article saved = articleRepository.save(article);
        mockMvc.perform(post("/api/articles/{slugUuid}/PUBLISH", "Title-" + saved.getId())
                .with(httpBasic("kamal1", "abc")))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void should_save_article_in_DRAFT_mode_by_default() throws Exception {
        ArticleRequest articleRequest = new ArticleRequest.Builder()
                .withTitle("let us study java")
                .withDescription("java is a programming language")
                .withBody("java is not javascript")
                .build();
        String json = objectMapper.writeValueAsString(articleRequest.toArticle());

        mockMvc.perform(post("/api/articles/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(httpBasic("kamal", "abc")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title").value("let us study java"))
                .andExpect(jsonPath("$.body").value("java is not javascript"))
                .andExpect(jsonPath("$.description").value("java is a programming language"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.status").value("DRAFT"))
                .andExpect(jsonPath("$.favorited").isBoolean())
                .andExpect(jsonPath("$.favoriteCount").value("0"));
    }

    @Test
    void should_return_an_articles_by_status_with_id() throws Exception {
        Article article = createArticle("Title", "Desc", "Body");
        article.setUser(user);
        Article saved = articleRepository.save(article);
        mockMvc.perform(get("/api/articles/{slugUuid}", "Title-" + saved.getId())
                .with(httpBasic("kamal", "abc")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title").value("Title"))
                .andExpect(jsonPath("$.body").value("Body"))
                .andExpect(jsonPath("$.description").value("Desc"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.status").value("DRAFT"))
                .andExpect(jsonPath("$.favorited").isBoolean())
                .andExpect(jsonPath("$.favoriteCount").value("0"));
    }

    @Test
    void should_compute_article_reading_time() throws Exception {
        Article article = new Article.Builder()
                .withBody("Body")
                .withTitle("Title")
                .withDescription("Desc")
                .build();
        article.setUser(user);
        Article saved = articleRepository.save(article);
        String id = saved.getSlug() + "-" + saved.getId();
        String uri = "/api/articles/" + id + "/timetoread";
        mockMvc.perform(get(uri)
                .with(httpBasic("kamal", "abc")))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.articleID").value(id))
                .andExpect(jsonPath("$.readTime.mins").isNotEmpty())
                .andExpect(jsonPath("$.readTime.seconds").isNotEmpty());
    }

    @Test
    void should_return_tag_metrics() throws Exception {
        Article article = new Article.Builder()
                .withBody("body")
                .withTitle("title")
                .withDescription("desc")
                .withTags(Arrays.asList("java", "Spring Boot", "tutorial")).build();
        article.setUser(user);
        Article saved = articleRepository.save(article);
        String uri = "/api/articles/tags";
        mockMvc.perform(get(uri)
                .with(httpBasic("kamal", "abc")))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].tag").value("java"))
                .andExpect(jsonPath("$[0].occurence").value(1));
    }

    @Test
    void should_mark_article_as_favorite() throws Exception {
        Article article = new Article.Builder()
                .withBody("body")
                .withTitle("title")
                .withDescription("desc")
                .withTags(Arrays.asList("tags", "java")).build();
        article.setUser(user);
        Article saved = articleRepository.save(article);
        String id = String.format("%s-%s", saved.getSlug(), saved.getId());
        this.mockMvc.perform(put("/api/articles/{slug_uuid}/favorited", id)
                .with(httpBasic("kamal", "abc")))
                .andDo(print())
                .andExpect(status().isNoContent());

    }

    @Test
    void should_mark_article_as_unfavorite() throws Exception {
        Article article = new Article.Builder()
                .withBody("body")
                .withTitle("title")
                .withDescription("desc")
                .withTags(Arrays.asList("tags", "java"))
                .withFavorited(true)
                .withFavoriteCount(5)
                .build();
        article.setUser(user);
        Article saved = articleRepository.save(article);
        String id = String.format("%s-%s", saved.getSlug(), saved.getId());
        this.mockMvc.perform(delete("/api/articles/{slug_uuid}/unfavorited", id)
                .with(httpBasic("kamal", "abc")))
                .andDo(print())
                .andExpect(status().isNoContent());

    }

    @Test
    void check_api_articles_post_request_status_as_created_with_image() throws Exception {
        ArticleRequest articleRequest = new ArticleRequest.Builder()
                .withBody("You have to believe")
                .withTitle("How to learn Spring Booot")
                .withDescription("Ever wonder how?").build();
        String mockJson = objectMapper.writeValueAsString(articleRequest);
        mockMvc.perform(post("/api/articles")
                .content(mockJson)
                .with(httpBasic("kamal", "abc"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.image").isNotEmpty())
                .andExpect(status().isCreated());
    }

    private Article createArticle(String title, String description, String body) {
        return new Article.Builder()
                .withTitle(title)
                .withDescription(description)
                .withBody(body)
                .build();
    }
}
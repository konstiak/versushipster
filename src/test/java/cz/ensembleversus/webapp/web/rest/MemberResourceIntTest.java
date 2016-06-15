package cz.ensembleversus.webapp.web.rest;

import cz.ensembleversus.webapp.VersushipsterApp;
import cz.ensembleversus.webapp.domain.Member;
import cz.ensembleversus.webapp.repository.MemberRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cz.ensembleversus.webapp.domain.enumeration.VoiceType;

/**
 * Test class for the MemberResource REST controller.
 *
 * @see MemberResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = VersushipsterApp.class)
@WebAppConfiguration
@IntegrationTest
public class MemberResourceIntTest {

    private static final String DEFAULT_FIRST_NAME = "AAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBB";
    private static final String DEFAULT_SECOND_NAME = "AAAAA";
    private static final String UPDATED_SECOND_NAME = "BBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final VoiceType DEFAULT_VOICE = VoiceType.SOPRANO;
    private static final VoiceType UPDATED_VOICE = VoiceType.MEZZOSOPRANO;
    private static final String DEFAULT_PHOTO = "AAAAA";
    private static final String UPDATED_PHOTO = "BBBBB";

    private static final LocalDate DEFAULT_MEMBER_FROM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MEMBER_FROM = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_MEMBER_TO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MEMBER_TO = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private MemberRepository memberRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMemberMockMvc;

    private Member member;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MemberResource memberResource = new MemberResource();
        ReflectionTestUtils.setField(memberResource, "memberRepository", memberRepository);
        this.restMemberMockMvc = MockMvcBuilders.standaloneSetup(memberResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        member = new Member();
        member.setFirstName(DEFAULT_FIRST_NAME);
        member.setSecondName(DEFAULT_SECOND_NAME);
        member.setActive(DEFAULT_ACTIVE);
        member.setVoice(DEFAULT_VOICE);
        member.setPhoto(DEFAULT_PHOTO);
        member.setMemberFrom(DEFAULT_MEMBER_FROM);
        member.setMemberTo(DEFAULT_MEMBER_TO);
    }

    @Test
    @Transactional
    public void createMember() throws Exception {
        int databaseSizeBeforeCreate = memberRepository.findAll().size();

        // Create the Member

        restMemberMockMvc.perform(post("/api/members")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(member)))
                .andExpect(status().isCreated());

        // Validate the Member in the database
        List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(databaseSizeBeforeCreate + 1);
        Member testMember = members.get(members.size() - 1);
        assertThat(testMember.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testMember.getSecondName()).isEqualTo(DEFAULT_SECOND_NAME);
        assertThat(testMember.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testMember.getVoice()).isEqualTo(DEFAULT_VOICE);
        assertThat(testMember.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testMember.getMemberFrom()).isEqualTo(DEFAULT_MEMBER_FROM);
        assertThat(testMember.getMemberTo()).isEqualTo(DEFAULT_MEMBER_TO);
    }

    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = memberRepository.findAll().size();
        // set the field null
        member.setFirstName(null);

        // Create the Member, which fails.

        restMemberMockMvc.perform(post("/api/members")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(member)))
                .andExpect(status().isBadRequest());

        List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSecondNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = memberRepository.findAll().size();
        // set the field null
        member.setSecondName(null);

        // Create the Member, which fails.

        restMemberMockMvc.perform(post("/api/members")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(member)))
                .andExpect(status().isBadRequest());

        List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVoiceIsRequired() throws Exception {
        int databaseSizeBeforeTest = memberRepository.findAll().size();
        // set the field null
        member.setVoice(null);

        // Create the Member, which fails.

        restMemberMockMvc.perform(post("/api/members")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(member)))
                .andExpect(status().isBadRequest());

        List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMembers() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the members
        restMemberMockMvc.perform(get("/api/members?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(member.getId().intValue())))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
                .andExpect(jsonPath("$.[*].secondName").value(hasItem(DEFAULT_SECOND_NAME.toString())))
                .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
                .andExpect(jsonPath("$.[*].voice").value(hasItem(DEFAULT_VOICE.toString())))
                .andExpect(jsonPath("$.[*].photo").value(hasItem(DEFAULT_PHOTO.toString())))
                .andExpect(jsonPath("$.[*].memberFrom").value(hasItem(DEFAULT_MEMBER_FROM.toString())))
                .andExpect(jsonPath("$.[*].memberTo").value(hasItem(DEFAULT_MEMBER_TO.toString())));
    }

    @Test
    @Transactional
    public void getMember() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get the member
        restMemberMockMvc.perform(get("/api/members/{id}", member.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(member.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.secondName").value(DEFAULT_SECOND_NAME.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.voice").value(DEFAULT_VOICE.toString()))
            .andExpect(jsonPath("$.photo").value(DEFAULT_PHOTO.toString()))
            .andExpect(jsonPath("$.memberFrom").value(DEFAULT_MEMBER_FROM.toString()))
            .andExpect(jsonPath("$.memberTo").value(DEFAULT_MEMBER_TO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMember() throws Exception {
        // Get the member
        restMemberMockMvc.perform(get("/api/members/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMember() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);
        int databaseSizeBeforeUpdate = memberRepository.findAll().size();

        // Update the member
        Member updatedMember = new Member();
        updatedMember.setId(member.getId());
        updatedMember.setFirstName(UPDATED_FIRST_NAME);
        updatedMember.setSecondName(UPDATED_SECOND_NAME);
        updatedMember.setActive(UPDATED_ACTIVE);
        updatedMember.setVoice(UPDATED_VOICE);
        updatedMember.setPhoto(UPDATED_PHOTO);
        updatedMember.setMemberFrom(UPDATED_MEMBER_FROM);
        updatedMember.setMemberTo(UPDATED_MEMBER_TO);

        restMemberMockMvc.perform(put("/api/members")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMember)))
                .andExpect(status().isOk());

        // Validate the Member in the database
        List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(databaseSizeBeforeUpdate);
        Member testMember = members.get(members.size() - 1);
        assertThat(testMember.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testMember.getSecondName()).isEqualTo(UPDATED_SECOND_NAME);
        assertThat(testMember.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testMember.getVoice()).isEqualTo(UPDATED_VOICE);
        assertThat(testMember.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testMember.getMemberFrom()).isEqualTo(UPDATED_MEMBER_FROM);
        assertThat(testMember.getMemberTo()).isEqualTo(UPDATED_MEMBER_TO);
    }

    @Test
    @Transactional
    public void deleteMember() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);
        int databaseSizeBeforeDelete = memberRepository.findAll().size();

        // Get the member
        restMemberMockMvc.perform(delete("/api/members/{id}", member.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(databaseSizeBeforeDelete - 1);
    }
}

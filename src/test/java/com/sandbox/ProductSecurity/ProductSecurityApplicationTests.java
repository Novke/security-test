package com.sandbox.ProductSecurity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductSecurityApplicationTests {

	@Autowired
	MockMvc mockMvc;
	@Test
	@WithMockUser(roles = {"USER", "ADMIN"})
	void test1() throws Exception {
		mockMvc.perform(post("/couponapi/coupons")
				.content("{\"code\":\"SUPERSALECSRF\",\"discount\":80.000,\"expDate\":\"12/12/2026\"}")
				.contentType(MediaType.APPLICATION_JSON)
						//NEW - CSRF
						.with(csrf().asHeader()))
				.andExpect(status().isOk());
	}

	@Test
	//NEW
	@WithUserDetails("doug@bailey.com")
	public void test2(){

	}

	@Test
	@WithMockUser(roles = {"USER"})
	public void test3() throws Exception {
		//NEW - CORS
		mockMvc.perform(options("/couponapi/coupons")
				.header("Access-Control-Request-Method", "POST")
				.header("Origin", "http://www.sajt.rs")).andExpect(status().isOk())
				.andExpect(header().exists("Access-Control-Allow-Origin"))
				.andExpect(header().string("Access-Control-Allow-Origin", "*"))
				.andExpect(header().exists("Access-Control-Allow-Origin"))
				.andExpect(header().string("Access-Control-Allow-Origin", "POST"));
	}
}

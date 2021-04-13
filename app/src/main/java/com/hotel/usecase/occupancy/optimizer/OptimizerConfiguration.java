package com.hotel.usecase.occupancy.optimizer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hotel.usecase.occupancy.InMemoryProspectGuestRepository;

@Configuration
class OptimizerConfiguration {

  @Bean
  OptimizerService optimizerService() {
    return new OptimizerService(new InMemoryProspectGuestRepository());
  }
}

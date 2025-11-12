package spring.practice.oct25.services;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import spring.practice.oct25.entities.User;

import java.util.stream.Stream;


public class UserArgumentProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return Stream.of(
                Arguments.of(User.builder().userName("lam").password("prem").build()),
                Arguments.of(User.builder().userName("pop").password("").build())
        );
    }
}

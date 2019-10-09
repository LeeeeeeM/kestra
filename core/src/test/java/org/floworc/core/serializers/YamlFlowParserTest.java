package org.floworc.core.serializers;

import org.floworc.core.exceptions.InvalidFlowException;
import org.junit.jupiter.api.Test;
import org.floworc.core.Utils;
import org.floworc.core.models.flows.Flow;
import org.floworc.core.models.tasks.RetryIntervalType;
import org.floworc.core.models.tasks.Task;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class YamlFlowParserTest {
    @Test
    void parse() throws IOException {
        Flow flow = Utils.parse("flows/valids/full.yaml");

        assertThat(flow.getId(), is("full"));
        assertThat(flow.getTasks().size(), is(5));

        // third with all optionals
        Task optionals = flow.getTasks().get(2);
        assertThat(optionals.getTimeout(), is(1000));
        assertThat(optionals.getRetry().getInterval().getSeconds(), is(900L));
        assertThat(optionals.getRetry().getType(), is(RetryIntervalType.CONSTANT));
        assertThat(optionals.getRetry().getLimit(), is(5));
    }

    @Test
    void validation() throws IOException {
        assertThrows(InvalidFlowException.class, () -> {
            Utils.parse("flows/invalids/invalid.yaml");
        });

        try {
            Utils.parse("flows/invalids/invalid.yaml");
        } catch (InvalidFlowException e) {
            assertThat(e.getViolations().size(), is(3));
        }
    }
}
package mini.jdbc.test.asset.dbi;

import mini.jdbc.Tx;
import mini.jdbc.test.asset.model.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SampleDbi {

    @Tx
    @Nullable
    User getUserByLogin(@NotNull String login);

}

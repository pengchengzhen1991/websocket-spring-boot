package com.siwei.darwin.repository.log;

import com.siwei.darwin.domain.Log;
import com.siwei.darwin.repository.BaseRepository;

public interface LogRepository extends BaseRepository<Log, Long>, LogRepositoryCustom {
}

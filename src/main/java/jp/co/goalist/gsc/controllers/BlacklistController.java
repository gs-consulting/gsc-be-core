package jp.co.goalist.gsc.controllers;

import jp.co.goalist.gsc.gen.apis.BlacklistsApi;
import jp.co.goalist.gsc.gen.dtos.*;
import jp.co.goalist.gsc.services.BlacklistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BlacklistController implements BlacklistsApi {

    private final BlacklistService blacklistService;

    @Override
    public ResponseEntity<Void> createNewBlacklist(BlacklistCreateDto blacklistCreateDto) {
        log.info("createNewBlacklist");
        blacklistService.createNewBlacklist(blacklistCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<Void> deleteSelectedBlacklists(SelectedIds selectedIds) {
        log.info("deleteSelectedBlacklists");
        blacklistService.deleteSelectedBlacklists(selectedIds);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> editBlacklistMemo(String id, BlacklistMemoDto blacklistMemoDto) {
        log.info("editBlacklistMemo");
        blacklistService.editBlacklistMemo(id, blacklistMemoDto);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<BlacklistListDto> getBlacklists(BlacklistSearchDto blacklistSearchDto) {
        log.info("getBlacklists");
        return ResponseEntity.ok(blacklistService.getBlacklist(blacklistSearchDto));
    }
}

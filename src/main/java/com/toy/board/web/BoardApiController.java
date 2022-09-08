package com.toy.board.web;

import java.util.List;

import com.toy.board.domain.Board;
import com.toy.board.reopsitory.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
class BoardApiController {


    @Autowired
    private BoardRepository boardRepository;



    @GetMapping("/board")
    List<Board> all() {
        return boardRepository.findAll();
    }


    @PostMapping("/board")
    Board newBoard(@RequestBody Board newBoard) {
        return boardRepository.save(newBoard);
    }

    // Single item

    @GetMapping("/board/{id}")
    Board one(@PathVariable Long id) {

        return boardRepository.findById(id).orElse(null);
    }

    @GetMapping("/boards")
    List<Board> findByTitle(@RequestParam(required = false, defaultValue = "") String title,
                            @RequestParam(required = false, defaultValue = "") String content) {
        if (StringUtils.isEmpty(title) && StringUtils.isEmpty(content)) {
            return boardRepository.findAll();
        } else {
            return boardRepository.findByTitleOrContent(title, content);
        }
    }

    @PostMapping("/boards")
    Board updateBoard(@RequestBody Board board) {
        return boardRepository.save(board);
    }


    @PutMapping("/boards/{id}")
    Board replaceBoard(@RequestBody Board board, @PathVariable Long id) {

        return boardRepository.findById(id)
                .map(Board -> {
                    Board.setTitle(board.getTitle());
                    Board.setContent(board.getContent());
                    return boardRepository.save(Board);
                })
                .orElseGet(() -> {
                    board.setId(id);
                    return boardRepository.save(board);
                });
    }

    @DeleteMapping("/boards/{id}")
    void deleteBoard(@PathVariable Long id) {
        boardRepository.deleteById(id);
    }
}
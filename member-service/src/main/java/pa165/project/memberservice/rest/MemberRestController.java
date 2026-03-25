package pa165.project.memberservice.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pa165.project.memberservice.dto.MemberDto;
import pa165.project.memberservice.dto.NewMemberRegisteringDto;
import pa165.project.memberservice.facade.MemberFacade;
import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "Member", description = "Member management API")
public class MemberRestController {

    private final MemberFacade memberFacade;

    @GetMapping
    @Operation(summary = "Get all members")
    public ResponseEntity<List<MemberDto>> getAllMembers() {
        return ResponseEntity.ok(memberFacade.getAllMembers());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get member by ID")
    public ResponseEntity<MemberDto> getMemberById(@PathVariable Long id) {
        return memberFacade.getMemberById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-email")
    @Operation(summary = "Get member by email")
    public ResponseEntity<MemberDto> getMemberByEmail(@RequestParam String email) {
        return memberFacade.getMemberWithEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-username")
    @Operation(summary = "Get member by username")
    public ResponseEntity<MemberDto> getMemberByUsername(@RequestParam String username) {
        return memberFacade.getMemberWithUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Register a new member")
    @GetMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public MemberDto registerMember(@Valid @RequestBody NewMemberRegisteringDto newMemberRegisteringDto) {
        return memberFacade.registerMember(newMemberRegisteringDto);
    }

    @PatchMapping("/activate/{id}")
    @Operation(summary = "Activate a member")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activateMember(@PathVariable Long id) {
        memberFacade.activateMember(id);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update a member")
    public MemberDto updateMember(@PathVariable Long id,
                                  @Valid @RequestBody MemberDto memberDto) {
        return memberFacade.updateMember(memberDto);
    }

    @DeleteMapping("/remove/{id}")
    @Operation(summary = "Remove a member")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeMember(@PathVariable Long id) {
        memberFacade.removeMember(id);
    }
}

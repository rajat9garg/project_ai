package com.learn.elasticsearch.controller

import com.learn.elasticsearch.model.User
import com.learn.elasticsearch.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: String): ResponseEntity<User> {
        return userService.getUserById(id)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @GetMapping("/email/{email}")
    fun getUserByEmail(@PathVariable email: String): ResponseEntity<User> {
        return userService.getUserByEmail(email)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @PostMapping
    fun createUser(@RequestBody user: User): ResponseEntity<User> {
        return ResponseEntity.ok(userService.createUser(user))
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: String, @RequestBody user: User): ResponseEntity<User> {
        return userService.updateUser(id, user)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: String): ResponseEntity<Unit> {
        userService.deleteUser(id)
        return ResponseEntity.ok().build()
    }

    @GetMapping
    fun getAllUsers(): ResponseEntity<List<User>> {
        return ResponseEntity.ok(userService.getAllUsers())
    }
} 
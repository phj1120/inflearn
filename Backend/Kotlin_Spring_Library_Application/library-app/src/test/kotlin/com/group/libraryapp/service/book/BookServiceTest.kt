package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class BookServiceTest @Autowired constructor(
    val bookRepository: BookRepository,
    val userRepository: UserRepository,
    val userLoanHistoryRepository: UserLoanHistoryRepository,
    val bookService: BookService
) {

    @AfterEach
    fun clean() {
        bookRepository.deleteAll()
        userRepository.deleteAll()
        // userLoanHistoryRepository 는 User 삭제 시 같이 삭제 됨. (orphanRemoval)
    }

    @Test
    @DisplayName("책 저장이 정상 동작한다.")
    fun saveBookTest() {
        // given
        val request = BookRequest("A")

        // when
        bookService.saveBook(request)

        // then
        val book = bookRepository.findAll()[0]
        assertThat(book.name).isEqualTo("A")
    }

    @Test
    @DisplayName("책 대출이 정상 동작한다.")
    fun loanBookTest() {
        // given
        bookRepository.save(Book("BookA"))
        val savedUser = userRepository.save(User("UserA", 20))
        val bookLoanRequest = BookLoanRequest("UserA", "BookA")

        // when
        bookService.loanBook(bookLoanRequest)

        // then
        val userLoanHistory = userLoanHistoryRepository.findAll()[0]
        assertThat(userLoanHistory.bookName).isEqualTo("BookA")
        assertThat(userLoanHistory.user.id).isEqualTo(savedUser.id)
        assertThat(userLoanHistory.isReturn).isFalse
    }

    @Test
    @DisplayName("책 대출 시 책 상태 검증이 정상 동작한다.")
    fun loanBookFailTest() {
        // given
        bookRepository.save(Book("BookA"))
        val userA = userRepository.save(User("UserA", 20))
        userRepository.save(User("UserB", 20))
        userLoanHistoryRepository.save(UserLoanHistory(userA, "BookA", false))
        val loanBookRequest = BookLoanRequest("UserB", "BookA")

        // when & then
        assertThrows<IllegalArgumentException> {
            bookService.loanBook(loanBookRequest)
        }.apply {
            assertThat(message).isEqualTo("진작 대출되어 있는 책입니다")
        }
    }

    @Test
    @DisplayName("책 대출 시 사용자 검증이 정상 동작한다.")
    fun loanBook_UserStatementTest() {
        // given
        bookRepository.save(Book("BookA"))
        val loanBookRequest = BookLoanRequest("UserA", "BookA")

        // when & then
        assertThrows<IllegalArgumentException> {
            bookService.loanBook(loanBookRequest)
        }
    }

    @Test
    @DisplayName("책 반납이 정상 동작한다.")
    fun returnBook() {
        // given
        bookRepository.save(Book("BookA"))
        userRepository.save(User("UserA", 20))
        val loanBookRequest = BookLoanRequest("UserA", "BookA")
        bookService.loanBook(loanBookRequest)
        val bookReturnRequest = BookReturnRequest("UserA", "BookA")

        // when
        bookService.returnBook(bookReturnRequest)

        // then
        val userLoanHistory = userLoanHistoryRepository.findAll()[0]
        assertThat(userLoanHistory.isReturn).isTrue()
    }

    @Test
    @DisplayName("책 반납 시 사용자 검증이 정상 동작한다.")
    fun returnBook_UserStatementTest() {
        // given
        val bookReturnRequest = BookReturnRequest("UserA", "BookA")

        // when & then
        assertThrows<IllegalArgumentException> {
            bookService.returnBook(bookReturnRequest)
        }
    }

}
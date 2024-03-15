use prometheus_exporter::{self, prometheus::register_counter};
use std::time::Instant;
use std::net::{IpAddr, Ipv4Addr, SocketAddr};

fn main() {
    let board_size = 32;

    let binding = SocketAddr::new(IpAddr::V4(Ipv4Addr::new(0, 0, 0, 0)), 7070);
    let counter = register_counter!("nqueen_solved_rust", "The number of times the problem was solved").unwrap();
    
    prometheus_exporter::start(binding).unwrap();

    loop {
        let mut board = vec![vec!['.'; board_size]; board_size];
        let start = Instant::now();
        solve_n_queens(&mut board, 0, 0, 0, 0);
        let duration = start.elapsed();
        counter.inc();
        println!("Time elapsed is: {:?}", duration);
    }
}

fn solve_n_queens(
    board: &mut Vec<Vec<char>>,
    row: usize,
    ld: usize, // left diagonal
    col: usize,
    rd: usize, // right diagonal
) -> bool {
    let size = board.len();
    if row == size {
        return true;
    }

    let mut available_positions = !(ld | col | rd) & ((1 << size) - 1);

    while available_positions != 0 {
        let pos = available_positions & (!available_positions + 1);
        let col_pos = pos.trailing_zeros() as usize;

        board[row][col_pos] = 'Q';
        if solve_n_queens(
            board,
            row + 1,
            (ld | pos) << 1,
            col | pos,
            (rd | pos) >> 1,
        ) {
            return true;
        }
        board[row][col_pos] = '.';

        available_positions ^= pos;
    }

    false
}

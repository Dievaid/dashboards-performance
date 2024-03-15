FROM rust:slim-bookworm as build-env

WORKDIR /app
COPY ./queen-problem-rust .

RUN cargo build --release

FROM ubuntu:22.04
WORKDIR /app
COPY --from=build-env /app/target/release/queen-problem .

ENTRYPOINT ["./queen-problem"]